package mffs;

public enum TransferMode {
    EQUALIZE,
    DISTRIBUTE,
    DRAIN,
    FILL;

    public TransferMode toggle() {
        int newOrdinal = this.ordinal() + 1;
        if (newOrdinal >= values().length) {
            newOrdinal = 0;
        }

        return values()[newOrdinal];
    }
}
