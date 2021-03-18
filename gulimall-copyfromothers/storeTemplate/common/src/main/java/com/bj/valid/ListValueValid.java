package com.bj.valid;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName ListValueValid
 * @Description TODO
 * @Author 13011
 * @Date 2020/8/29 23:43
 * @Version 1.0
 **/
public class ListValueValid implements ConstraintValidator<ListValue,Integer> {
    //此处用set集合防止多线程情况下结合插入过多数据
   private Set<Integer> set=new HashSet<>();
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        if(vals!=null&&vals.length>0){
            Arrays.stream(vals).forEach(value -> {
                set.add(value);
            });
        }

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(set.contains(value)){
            return true;
        }
        return false;
    }
}
