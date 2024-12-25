package mffs;

import universalelectricity.core.UniversalElectricity;

public class ConversionHelper {
    public static double toEU(double joules) {
        return joules / UniversalElectricity.UE_IC2_RATIO;
    }

    public static double fromEU(double eu) {
        return eu * UniversalElectricity.UE_IC2_RATIO;
    }

    public static int toRF(double joules) {
        return (int) (joules / UniversalElectricity.UE_RF_RATIO);
    }

    public static double fromRF(int rf) {
        return rf * UniversalElectricity.UE_RF_RATIO;
    }
}
