package com.CM.CookingMenu.foodmenu.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "foodmenu_attendance")
public class FoodMenuAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int foodmenuAttendanceId;

    @Column(nullable = false)
    private int foodmenuId;

    @Column(nullable = false)
    private int userId;
}
