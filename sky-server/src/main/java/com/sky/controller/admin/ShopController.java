package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "店铺相关接口")
@RestController("adminShopController")
@RequestMapping("/admin/shop")
public class ShopController {
    private static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation("设置店铺营业状态")
    @PostMapping("/{status}")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态为： {}", status == 1 ? "营业中" : "已打烊");
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set(KEY, status);
        return Result.success();
    }

    @ApiOperation("查询店铺营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        ValueOperations ops = redisTemplate.opsForValue();
        Integer status = (Integer)ops.get(KEY);
        log.info("获取店铺营业状态为： {}", status == 1 ? "营业中" : "已打烊");
        return Result.success(status);
    }

}
