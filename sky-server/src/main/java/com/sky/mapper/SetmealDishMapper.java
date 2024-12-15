package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<Long> getSetmealIdsByIds(List<Long> dishIds);


    void insertBatch(List<SetmealDish> setmealDishes);

    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void delete(Long id);

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetmealId(Long id);

    @Select("select sd.* from setmeal_dish sd left outer join dish d on sd.dish_id = d.id where sd.setmeal_id = #{id} and d.status = 0")
    List<SetmealDish> getBySetmealIdWithStatus(Long id);
}
