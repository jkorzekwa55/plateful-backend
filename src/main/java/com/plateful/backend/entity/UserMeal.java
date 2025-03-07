package com.plateful.backend.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Table("meal_by_user_id")
@Data
public class UserMeal {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID userId;
    @PrimaryKeyColumn
    private UUID mealId;
    private String title;
    private String description;
    private byte[] mealImage;
    private List<String> ingredients;
    private String recipeLink;
    private LocalDate lastPrepared;
}
