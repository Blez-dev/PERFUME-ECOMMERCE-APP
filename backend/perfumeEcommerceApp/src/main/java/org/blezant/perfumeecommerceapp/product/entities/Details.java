package org.blezant.perfumeecommerceapp.product.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "details")
public class Details {
    @Id
    private String pid;
    private int reviews_no;

    private BigDecimal stars_avg;
}
