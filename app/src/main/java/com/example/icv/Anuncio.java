package com.example.icv;


import java.util.ArrayList;

public class Anuncio {

   public String id_anuncio;
   public String titulo;
   public int year;
   public String modelo;
   public String marca;
   public String id_usuario;
   public double precio;
   public String estado;
   public String descripcion;
   public ArrayList<String> listImg;
   public String fecha_publicacion;
   public String id_favorito;


   public Anuncio(String id_anuncio, String titulo, int year, String modelo, String marca, String id_usuario, double precio, String estado, String descripcion, ArrayList <String> listImg, String fecha_publicacion, String id_favorito) {
      this.id_anuncio = id_anuncio;
      this.titulo = titulo;
      this.year = year;
      this.modelo = modelo;
      this.marca = marca;
      this.id_usuario = id_usuario;
      this.precio = precio;
      this.estado = estado;
      this.descripcion = descripcion;
      this.listImg = listImg;
      this.fecha_publicacion = fecha_publicacion;
      this.id_favorito = id_favorito;
   }

   public String getId_favorito() {
      return id_favorito;
   }
   public String getId_anuncio() {
      return id_anuncio;
   }

   public String getTitulo() {
      return titulo;
   }

   public int getYear() {
      return year;
   }

   public String getModelo() {
      return modelo;
   }

   public String getMarca() {
      return marca;
   }

   public String getId_usuario() {
      return id_usuario;
   }

   public double getPrecio() {
      return precio;
   }

   public String getEstado() {
      return estado;
   }

   public String getDescripcion() {
      return descripcion;
   }

   public ArrayList<String> getListImg() {
      return listImg;
   }

   public String getFecha_publicacion() {
      return fecha_publicacion;
   }
}
