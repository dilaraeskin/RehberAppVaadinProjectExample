package com.uniyaz;

import javax.servlet.annotation.WebServlet;

import com.uniyaz.db.DBTransaction;
import com.uniyaz.domain.Person;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;


import java.util.Base64;
import java.util.List;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.uniyaz.MyAppWidgetset")
public class MyUI extends UI {

    Person person = new Person();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        DBTransaction dbTransaction = new DBTransaction();

        FormLayout formLayout = new FormLayout();

        RehberTextField nameField = new RehberTextField();
        nameField.setCaption("Ad:");
        formLayout.addComponent(nameField);

        RehberTextField surnameField = new RehberTextField();
        surnameField.setCaption("Soyad:");
        formLayout.addComponent(surnameField);

        RehberTextField phoneField = new RehberTextField();
        phoneField.setCaption("Telefon:");
        formLayout.addComponent(phoneField);


        Button kaydet = new Button();
        kaydet.setCaption("Kişi Ekle");
        kaydet.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                Person person = new Person();
                person.setName(nameField.getValue());
                person.setSurname(surnameField.getValue());
                person.setPhone(phoneField.getValue());

                //        DBTransaction dbTransaction=new DBTransaction();
                dbTransaction.addPerson(person);
                Notification.show("Kişi Eklendi");
            }
        });

        Button listele = new Button();
        listele.setCaption("Rehberi Listele");
        listele.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                List<Person> personList = dbTransaction.personList();

                Grid grid=new Grid();
                grid.getContainerDataSource().removeAllItems();

                BeanItemContainer<Person> personContainer = new BeanItemContainer<Person>(Person.class, personList);
                grid = new Grid("Kişiler", personContainer);
                liste(grid, formLayout);


                Notification.show("Listelendi");

            }
        });

        Button sil = new Button();
        sil.setCaption("Kişi Sil");
        sil.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {


          //      person.setName(nameField.getValue()); //isim ile silmek mantıklı olmayacağı için kullanmadım.


                List<Person> personList = dbTransaction.personList();

                Grid grid=new Grid();


                BeanItemContainer<Person> personContainer = new BeanItemContainer<Person>(Person.class, personList);
                grid = new Grid("Kişiler", personContainer);
                Object selected = grid.getSelectionModel().getSelectedRows();
                int i= (int) grid.getContainerDataSource().getItem(selected).getItemProperty("id").getValue();
                System.out.println(i);
                person.setId(i);



                dbTransaction.deletePerson(person);
                Notification.show("Kişi Silindi");
            }

        });

        Button guncelle = new Button();
        guncelle.setCaption("Kişi Bilgileri Güncelle");
        guncelle.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                Person person = new Person();
                person.setName(nameField.getValue());
                person.setSurname(surnameField.getValue());
                person.setPhone(phoneField.getValue());

                dbTransaction.updatePerson(person);
                Notification.show("Kişi Güncelle");
            }
        });


        Button ara = new Button();
        ara.setCaption("Kişi Arama");
        ara.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                person.setName(nameField.getValue());

                List<Person> personList = dbTransaction.searchPerson(person);

                Grid grid=new Grid();
                grid.getContainerDataSource().removeAllItems();

                BeanItemContainer<Person> personContainer = new BeanItemContainer<Person>(Person.class, personList);
                grid = new Grid("Kişiler", personContainer);

                liste(grid, formLayout);
                Notification.show("Kişi Arandı");


            }
        });

        formLayout.addComponent(sil);
        formLayout.addComponent(guncelle);
        formLayout.addComponent(ara);
        formLayout.addComponent(kaydet);
        formLayout.addComponent(listele);


        setContent(formLayout);

    }

    private void liste(Grid grid, FormLayout formLayout) {

      //  grid.getContainerDataSource().removeAllItems();
        //Başlıkların isimlerini değiştirdim
        grid.getColumn("id").setHeaderCaption("ID");
        grid.getColumn("name").setHeaderCaption("Ad");
        grid.getColumn("surname").setHeaderCaption("Soyad");
        grid.getColumn("phone").setHeaderCaption("Telefon");
        grid.setColumnOrder("id", "name", "surname", "phone");

        //Başlıklara boyut verdim. Tablonun daha güzel durması için önemli
        grid.getColumn("id").setMaximumWidth(60);
        grid.getColumn("name").setMinimumWidth(100);
        grid.getColumn("surname").setMinimumWidth(100);
        grid.getColumn("phone").setMinimumWidth(100);

//                //Tabloda bir veriye tıklayınca aşağıda seçenek kısmı çıkıyor
//                grid.setEditorEnabled(true);
          //      grid.setFrozenColumnCount(0);

        formLayout.addComponent(grid);


    }


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
