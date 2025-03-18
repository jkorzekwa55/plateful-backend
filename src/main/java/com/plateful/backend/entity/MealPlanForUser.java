package com.plateful.backend.entity;

import com.plateful.backend.model.MealType;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Table("meal_plan_by_user")
@Data
public class MealPlanForUser {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID userId;
    @PrimaryKeyColumn(ordinal = 0)
    private LocalDate mealDate;
    @PrimaryKeyColumn(ordinal = 1)
    private UUID mealId;
    private String title;
    private String description;
    @CassandraType(type = CassandraType.Name.BLOB)
    private byte[] mealImage;
    private List<String> ingredients;
    private String recipeLink;
    private MealType mealType;
    private Boolean accepted = Boolean.FALSE;
}
