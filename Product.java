package main;

import java.sql.Date;

public class Product {
        private int id;
        private String name;
        private float price;
        private Date DateAdded;
        private byte[] picture;

        public Product(int pid,String pname,float pprice, Date pAddDate,byte[] img) {
                id = pid;
                name = pname;
                price = pprice;
                DateAdded = pAddDate;
                picture=img;
        }

        public int getId() {
                return id;
        }

        public String getName(){
                return name;
        }

        public Date getDateAdded(){
                return DateAdded;
        }

        public float getPrice(){
                return price;
        }

        public byte[] getPicture(){
                return picture;
        }


}