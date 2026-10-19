package org.blezant.perfumeecommerceapp.product.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "perfumes")
public class Perfumes {
    private String name;
    private Long quantities;
    private String imageKey;
    private String description;
    private String category;
    @Id
    private String pid;
    @Column(nullable = false,precision = 19,scale = 2)
    private BigDecimal price;
    @OneToMany(mappedBy = "perfumes",fetch = FetchType.LAZY)
    List<Reviews> reviews= new ArrayList<>();
}
