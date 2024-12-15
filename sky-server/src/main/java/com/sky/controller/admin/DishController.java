package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;


    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishdto) {
        log.info("新增菜品：{}", dishdto);
        dishService.save(dishdto);
        return Result.success();
    }



    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("查询菜品：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);


        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("删除菜品：{}", ids);
        dishService.deleteBatch(ids);


        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);

        DishVO dishVO = dishService.getByIdWithFlavor(id);


        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishdto) {
        log.info("修改菜品：{}", dishdto);

        dishService.updateWithFlavor(dishdto);


        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用菜品")
    public Result enableOrDisable(@PathVariable("status") Integer status, Long id) {
        log.info("启用禁用菜品{} {}", status,id);
        dishService.enableOrDisable(status,id);

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> getByCategoryId(@RequestParam Long id) {
        log.info("根据分类id查询菜品：{}", id);

        List<Dish> result = dishService.getByCategoryId(id);


        return Result.success(result);
    }

}
