package com.cico643.simplebanking.model;

public enum TransactionType {
    DEPOSIT_TRANSACTION(Values.DEPOSIT_TRANSACTION),
    WITHDRAWAL_TRANSACTION(Values.WITHDRAWAL_TRANSACTION),
    PHONE_BILL_PAYMENT_TRANSACTION(Values.PHONE_BILL_PAYMENT_TRANSACTION),
    CHECK_TRANSACTION(Values.CHECK_TRANSACTION);
    private String value;
    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static class Values {
        public static final String DEPOSIT_TRANSACTION = "DepositTransaction";
        public static final String WITHDRAWAL_TRANSACTION = "WithdrawalTransaction";
        public static final String PHONE_BILL_PAYMENT_TRANSACTION = "PhoneBillPaymentTransaction";
        public static final String CHECK_TRANSACTION = "CheckTransaction";
    }
}
