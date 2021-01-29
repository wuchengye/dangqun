package com.dangqun.controller.common;

import com.dangqun.constant.Constants;
import com.dangqun.entity.AuthEntity;
import com.dangqun.entity.BranchEntity;
import com.dangqun.entity.UserEntity;
import com.dangqun.service.AuthService;
import com.dangqun.service.BranchService;
import com.dangqun.service.TrackService;
import com.dangqun.service.UserService;
import com.dangqun.service.common.RedisService;
import com.dangqun.vo.restful.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wcy
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private AuthService authService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private UserService userService;

    @PostMapping("/getBranchAndTrack")
    public Result getBranchAndTrack(){
        UserEntity userEntity = redisService.getUserData();
        AuthEntity authEntity = authService.selectOneById(userEntity.getUserAuth());
        if(authEntity == null){
            return Result.failure("权限检测失败");
        }
        switch (authEntity.getAuthLevel()){
            case Constants.AUTH_LEVEL_ADMIN:
                List<Map> adminList = new ArrayList<>();
                List<BranchEntity> allBranch = branchService.selectAll();
                for (BranchEntity b : allBranch){
                    Map<String,Object> map = new HashMap<>(3);
                    map.put("branch",b);
                    map.put("track",trackService.selectAllByBranch(b.getBranchId()));
                    map.put("isOwn",true);
                    adminList.add(map);
                }
                return Result.success(adminList);
            case Constants.AUTH_LEVEL_COMMON:
                BranchEntity branch = branchService.selectOneById(userEntity.getUserBranch());
                Map<String,Object> map = new HashMap<>(3);
                map.put("branch",branch);
                map.put("track",trackService.selectAllByBranch(branch.getBranchId()));
                map.put("isOwn",true);
                return Result.success(map);
            case Constants.AUTH_LEVEL_SECONDARY:
                BranchEntity ownBranch = branchService.selectOneById(userEntity.getUserBranch());
                List<Map> commList = new ArrayList<>();
                String[] branches = authEntity.getAuthBranch().split(";");
                String[] tracks = authEntity.getAuthBranchPath().split(";");
                for(int index = 0; index < branches.length; index++){
                    Map<String ,Object> map1 = new HashMap<>(3);
                    int b = Integer.parseInt(branches[index]);
                    if(b == ownBranch.getBranchId()){
                        map1.put("branch",ownBranch);
                        map1.put("track",trackService.selectAllByBranch(b));
                        map1.put("isOwn",true);
                        commList.add(map1);
                    }else {
                        String[] ts = tracks[index].split(",");
                        //in 查找路径
                    }
                }
                return Result.failure();
            default:
                return Result.failure();
        }
    }
}
