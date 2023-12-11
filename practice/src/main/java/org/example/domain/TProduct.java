package org.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName t_product
 */
@TableName(value ="t_product")
@Data
public class TProduct implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String product_name;

    /**
     * 
     */
    private String product_title;

    /**
     * 
     */
    private String product_img;

    /**
     * 
     */
    private String product_detail;

    /**
     * 
     */
    private BigDecimal product_price;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TProduct other = (TProduct) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProduct_name() == null ? other.getProduct_name() == null : this.getProduct_name().equals(other.getProduct_name()))
            && (this.getProduct_title() == null ? other.getProduct_title() == null : this.getProduct_title().equals(other.getProduct_title()))
            && (this.getProduct_img() == null ? other.getProduct_img() == null : this.getProduct_img().equals(other.getProduct_img()))
            && (this.getProduct_detail() == null ? other.getProduct_detail() == null : this.getProduct_detail().equals(other.getProduct_detail()))
            && (this.getProduct_price() == null ? other.getProduct_price() == null : this.getProduct_price().equals(other.getProduct_price()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProduct_name() == null) ? 0 : getProduct_name().hashCode());
        result = prime * result + ((getProduct_title() == null) ? 0 : getProduct_title().hashCode());
        result = prime * result + ((getProduct_img() == null) ? 0 : getProduct_img().hashCode());
        result = prime * result + ((getProduct_detail() == null) ? 0 : getProduct_detail().hashCode());
        result = prime * result + ((getProduct_price() == null) ? 0 : getProduct_price().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", product_name=").append(product_name);
        sb.append(", product_title=").append(product_title);
        sb.append(", product_img=").append(product_img);
        sb.append(", product_detail=").append(product_detail);
        sb.append(", product_price=").append(product_price);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}