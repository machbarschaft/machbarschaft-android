package com.ks.einanrufhilft.persistance;

public class OrderDTO {
    private String phone_number;
    private String adress;
    private String first_name;
    private String last_name;
    private String category;
    private String einkaufszettel;
    private String id;

    public OrderDTO(String phone_number, String adress, String first_name, String last_name, String category, String einkaufszettel, String id){
        this.phone_number = phone_number;
        this.adress = adress;
        this.first_name = first_name;
        this.last_name = last_name;
        this.category = category;
        this.einkaufszettel = einkaufszettel;
        this.id = id;
    }

    public OrderDTO(){
        this.phone_number = "123";
        this.adress = "Adresse";
        this.first_name = "Muster";
        this.last_name ="Name";
        this.category = "Einkauf";
        this.einkaufszettel = "Nudeln, Soße, Käse";
        this.id = "1";
    }
}
