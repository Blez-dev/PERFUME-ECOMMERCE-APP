package org.blezant.perfumeecommerceapp.product.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PerfumeDetailsResponseDto {
    String imageUrl;
    String perfumeName;
    BigDecimal price;
    String category;
    BigDecimal starsAvg;
    String starsReviews;
    String description;
    List<UsersComment> usersComment;
}


