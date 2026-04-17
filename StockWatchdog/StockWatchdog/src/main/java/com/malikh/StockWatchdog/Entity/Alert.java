package com.malikh.StockWatchdog.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    
    public enum Condition {
        ABOVE,
        BELOW
    }
    private Condition condition;
    private Double value;
    private Boolean isTrue;
    @ManyToOne
    private Stock stock;
    @ManyToOne
    private User user;

    public Alert(Long id, Condition condition, Double value, Boolean istrue, Stock s, User u){
        this.id = id;
        this.condition = condition;
        this.value = value;
        this.isTrue = istrue;
        this.stock = s;
        this.user = u;
    }
}
