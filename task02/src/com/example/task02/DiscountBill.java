package com.example.task02;

public class DiscountBill extends  Bill
{
    private Double Discount;

    public DiscountBill(double discount)
    {
        this.Discount = discount;
    }

    public void setDiscount(double discount)
    {
        this.Discount = discount;
    }

    public String  getDiscount()
    {
        return Discount * 100 + "%";
    }

    @Override
    public long getPrice()
    {
        return (long)(super.getPrice() * (1 - Discount));
    }

    public long getAbsDiscount()
    {
        return super.getPrice() - this.getPrice();
    }

}