package com.coffeehouse.model.entity;

import java.util.List;

public class OrderRequest {
    private String deskId;
    private List<DrinkId> drinkIds;
    private String employeeId;

    public String getDeskId() {
        return deskId;
    }

    public void setDeskId(String value) {
        this.deskId = value;
    }

    public List<DrinkId> getDrinkIds() {
        return drinkIds;
    }

    public void setDrinkIds(List<DrinkId> value) {
        this.drinkIds = value;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String value) {
        this.employeeId = value;
    }

    public static class DrinkId {
        private long count;
        private long drinkId;

        public long getCount() {
            return count;
        }

        public void setCount(long value) {
            this.count = value;
        }

        public long getDrinkId() {
            return drinkId;
        }

        public void setDrinkId(long value) {
            this.drinkId = value;
        }
    }
}
