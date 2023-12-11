package org.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

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
    private String productName;

    /**
     * 
     */
    private String productTitle;

    /**
     * 
     */
    private String productImg;

    /**
     * 
     */
    private String productDetail;

    /**
     * 
     */
    private BigDecimal productPrice;

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
            && (this.getProductName() == null ? other.getProductName() == null : this.getProductName().equals(other.getProductName()))
            && (this.getProductTitle() == null ? other.getProductTitle() == null : this.getProductTitle().equals(other.getProductTitle()))
            && (this.getProductImg() == null ? other.getProductImg() == null : this.getProductImg().equals(other.getProductImg()))
            && (this.getProductDetail() == null ? other.getProductDetail() == null : this.getProductDetail().equals(other.getProductDetail()))
            && (this.getProductPrice() == null ? other.getProductPrice() == null : this.getProductPrice().equals(other.getProductPrice()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProductName() == null) ? 0 : getProductName().hashCode());
        result = prime * result + ((getProductTitle() == null) ? 0 : getProductTitle().hashCode());
        result = prime * result + ((getProductImg() == null) ? 0 : getProductImg().hashCode());
        result = prime * result + ((getProductDetail() == null) ? 0 : getProductDetail().hashCode());
        result = prime * result + ((getProductPrice() == null) ? 0 : getProductPrice().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productName=").append(productName);
        sb.append(", productTitle=").append(productTitle);
        sb.append(", productImg=").append(productImg);
        sb.append(", productDetail=").append(productDetail);
        sb.append(", productPrice=").append(productPrice);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}