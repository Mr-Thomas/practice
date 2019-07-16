package com.tj.practice.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class User implements Serializable {

    public enum Flag {
        MAN,WOMAN
    };

    private int id;
    private String name;
    private BigDecimal amount = new BigDecimal(100);
    private Flag flag;

    public User() {
    }

    public User(int id, String name, Flag flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(int id, String name, BigDecimal amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }
    /*@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", amount=" + amount +
				'}';
	}*/

    public String toString() {
        return new StringBuilder()
                .append("{")
                .append("id=").append(id)
                .append(",name=\"").append(name).append("\"")
                .append("}")
                .toString();
    }
}
