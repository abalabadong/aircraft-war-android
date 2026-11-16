package edu.hitsz.factory;

import edu.hitsz.supply.*;


public class SupplyFactory {

    private SupplyFactory() {
    }

    public static AbstractSupply createSupply(String type, int x, int y) {
        switch (type) {
            case "blood":
                return new BloodSupply(x, y, 0, 3);
            case "fire":
                return new FireSupply(x, y, 0, 3);
            case "firePlus":
                return new FirePlusSupply(x, y, 0, 3);
            case "bomb":
                return new BombSupply(x, y, 0, 3);
            case "freeze":
                return new FreezeSupply(x, y, 0, 3);
            default:
                throw new IllegalArgumentException("Unknown supply type: " + type);
        }
    }
}
