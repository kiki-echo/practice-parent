package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.TProduct;
import org.example.mapper.TProductMapper;
import org.example.service.TProductService;
import org.springframework.stereotype.Service;

/**
* @author 28069
* @description 针对表【t_product】的数据库操作Service实现
* @createDate 2023-12-11 15:48:01
*/
@Service
public class TProductServiceImpl extends ServiceImpl<TProductMapper, TProduct>
    implements TProductService {

}




