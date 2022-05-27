package org.balicki.Ahorcado.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Palabra {
    private final static String FICHERO = "./src/main/resources/static/txt/ahorcado.txt";
    private String palabra;
    private char[] arrayPalabra;
    private int intentos = 1;
    private int fallos = 6;
    private Map<Integer, String> listaFallos = new HashMap<Integer, String>();

    public Palabra() throws IOException {
        // Seleccion aleatoria de una palabra
        palabra = seleccionaPalabra();
        arrayPalabra = new char[palabra.length()];
        // Conversion de la palabra seleccionada en un guiones
        for (int i = 0; i < arrayPalabra.length; i++) {
            arrayPalabra[i] = '-';
        }
        listaFallos.put(6, "uno");
        listaFallos.put(5, "dos");
        listaFallos.put(4, "tres");
        listaFallos.put(3, "cuatro");
        listaFallos.put(2, "cinco");
        listaFallos.put(1, "seis");
        listaFallos.put(0, "siete");
    }

    public String seleccionaPalabra() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(FICHERO));
        int random = 0;
        while ((palabra = br.readLine()) != null) {
            random++;
        }
        random = (int) (Math.random() * random);
        br.close();
        br = new BufferedReader(new FileReader(FICHERO));
        for (int i = 0; i < random; i++) {
            palabra = br.readLine().toLowerCase();
        }
        return palabra;
    }

    public void compruebaPalabra(String letra) {
        boolean encontrado = false;
        if (palabra.contains(letra)) {
            for (int i = 0; i < arrayPalabra.length; i++) {
                if (palabra.charAt(i) == letra.charAt(0)) {
                    arrayPalabra[i] = palabra.charAt(i);
                    encontrado = true;
                }
            }
        }
        if (encontrado) {
            intentos++;
        } else {
            intentos++;
            fallos--;
        }
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public char[] getArrayPalabra() {
        return arrayPalabra;
    }

    public void setArrayPalabra(char[] arrayPalabra) {
        this.arrayPalabra = arrayPalabra;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public int getFallos() {
        return fallos;
    }

    public void setFallos(int fallos) {
        this.fallos = fallos;
    }

    public Map<Integer, String> getListaFallos() {
        return listaFallos;
    }

    public void setListaFallos(Map<Integer, String> listaFallos) {
        this.listaFallos = listaFallos;
    }
}
