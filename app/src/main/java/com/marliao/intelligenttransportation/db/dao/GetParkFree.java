package com.marliao.intelligenttransportation.db.dao;

import java.util.List;

public class GetParkFree {
    public List<ParkFreeId> getParkFreeIdLit() {
        return parkFreeIdLit;
    }

    public void setParkFreeIdLit(List<ParkFreeId> parkFreeIdLit) {
        this.parkFreeIdLit = parkFreeIdLit;
    }

    private List<ParkFreeId> parkFreeIdLit;


}
