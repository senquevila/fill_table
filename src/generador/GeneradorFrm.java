/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JCheckBox;

/**
 *
 * @author usuario
 */
public class GeneradorFrm extends JFrame {

    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JList list1;
    private JTextField text1;
    private JTextField text2;
    private JTextField text3;
    private JButton button1;
    private JButton button2;
    private DefaultListModel listModel;
    private JScrollPane scroll1;
    private JCheckBox checkNombre;
    private JCheckBox checkApellido;
    private JCheckBox checkEmail;
    private JCheckBox checkTipoSangre;
    private JCheckBox checkFecha;
    private JCheckBox checkCiudad;
    private JCheckBox checkTelefono;
    private JCheckBox checkAltura;
    private JCheckBox checkPeso;
    private JCheckBox checkAll;

    private ArrayList<String> nombres;
    private ArrayList<String> apellidos;
    
    private int mCommit;
    private int mColumnas;
    private int mID;

    public GeneradorFrm() {
        iniciarComponentes();

        button1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mColumnas = Integer.parseInt(text1.getText());
                    mCommit = Integer.parseInt(text2.getText());
                    mID = Integer.parseInt(text3.getText());
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    mColumnas = 1;
                    mCommit = 500;
                    mID = 1;
                }
                listModel.clear();
                generarColumnas();
                
                System.out.println("Columnas generadas");
            }
        });

        button2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String path = "C:\\data\\result.txt";
                BufferedWriter bw = null;

                try {
                    File f = new File(path);
                    if (f.exists()) {
                        f.delete();
                    }
                    
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "ISO-8859-1"));
                    
                    for (int i = 0; i < listModel.size(); i++) {
                        bw.write(listModel.get(i).toString());
                        bw.newLine();
                    }
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                } finally {
                    try {
                        bw.close();
                    } catch (IOException ex) {
                        System.out.println("IOException: " + ex.getMessage());
                    }
                }
                
                System.out.println("Escritura terminada");
            }
        });
        
        checkAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (Component component : panel2.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox check = (JCheckBox) component;
                        check.setSelected(checkAll.isSelected());
                    }
                }
            }
        });
    }

    ArrayList<String> cargarArchivo(String path) throws FileNotFoundException, IOException {
        BufferedReader br;
        ArrayList lista = new ArrayList<>();
        String linea;

        br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "ISO-8859-1"));

        while ((linea = br.readLine()) != null) {
            lista.add(linea.trim());
        }

        br.close();

        return lista;
    }

    void generarColumnas() {
        String[] tipoSangre = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        String[] dominios = {"gmail", "yahoo", "outlook", "rocketmail"};
        String columna, nombre, apellido, sql;
        int indice;
        Random rnd = new Random();

        try {
            nombres = cargarArchivo("C:\\data\\nombres.txt");
            apellidos = cargarArchivo("C:\\data\\apellidos.txt");
            nombre = "";
            apellido = "";

            for (int i = 0; i < mColumnas; i++) {
                columna = "";
                sql = "INSERT INTO Empleados (id";
                columna += String.valueOf(mID++);

                if (checkNombre.isSelected()) {                    
                    indice = rnd.nextInt(nombres.size());
                    nombre = nombres.get(indice);
                    sql += ", nombre1";
                    columna += ", '" + nombre + "'";
                    
                    int tamaño = nombre.length();
                    int ini = tamaño - 2;
                    int fin = tamaño - 1;
                                        
                    sql += ", genero";
                    
                    String genero = "";
                    if (nombre.substring(ini, fin).equals("a")) {
                        genero = "F";
                    } else {
                        genero = "M";
                    }
                    columna += ", '" + genero + "'";
                }

                if (checkApellido.isSelected()) {
                    indice = rnd.nextInt(apellidos.size());
                    apellido = apellidos.get(indice);
                    sql += ", apellido1";
                    columna += ", '" + apellido + "'";
                }

                if (checkEmail.isSelected()) {
                    indice = rnd.nextInt(dominios.length);
                    sql += ", email";
                    columna += ", '" + nombre + "." + apellido + "@"
                            + dominios[indice] + ".com'";
                }

                if (checkFecha.isSelected()) {
                    int año = 2015 - rnd.nextInt(40) + 18;
                    int mes = rnd.nextInt(12) + 1;
                    int dia = rnd.nextInt(28) + 1;

                    String fecha = "TO_DATE('" + String.valueOf(año) + "-"
                            + String.valueOf(mes) + "-"
                            + String.valueOf(dia) + "', 'YYYY-MM-DD')";
                    sql += ", fecha_nacimiento";
                    columna += "," + fecha;
                }

                if (checkTipoSangre.isSelected()) {
                    indice = rnd.nextInt(tipoSangre.length);
                    sql += ", tipo_sangre";
                    columna += ", '" + tipoSangre[indice] + "'";
                }

                if (checkAltura.isSelected()) {
                    indice = rnd.nextInt(60) + 150;
                    sql += ", altura";
                    columna += "," + indice;
                }

                if (checkPeso.isSelected()) {
                    double d = (rnd.nextDouble() + 0.8) * 100.0;
                    sql += ", peso";
                    columna += "," + d;
                }

                if (checkTelefono.isSelected()) {
                    sql += ", telefono";
                    columna += ", '";
                    indice = rnd.nextInt(200) + 600;
                    columna += indice;
                    indice = rnd.nextInt(999);
                    columna += " " + indice;
                    indice = rnd.nextInt(999);
                    columna += " " + indice + "'";
                }
                
                if (checkCiudad.isSelected()) {
                    sql += ", ciudad_id";
                    indice = rnd.nextInt(51) + 1;
                    columna += ", " + indice;
                }

                sql += ") VALUES (" + columna + ");";
                
                if (i % mCommit == 0 || i == mColumnas) {
                    sql += "COMMIT;";
                }
                
                listModel.addElement(sql);
            }

            list1.setModel(listModel);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void iniciarComponentes() {
        panel1 = new JPanel();
        panel3 = new JPanel();
        panel2 = new JPanel();
        panel4 = new JPanel();
        label1 = new JLabel("Columnas");
        label2 = new JLabel("Commit");
        label3 = new JLabel("ID");
        list1 = new JList();
        text1 = new JTextField();
        text2 = new JTextField("500");
        text3 = new JTextField("1");
        button1 = new JButton("Generar");
        button2 = new JButton("Guardar");
        listModel = new DefaultListModel();
        scroll1 = new JScrollPane();
        checkNombre = new JCheckBox("Nombre");
        checkApellido = new JCheckBox("Apellido");
        checkEmail = new JCheckBox("Email");
        checkTipoSangre = new JCheckBox("Tipo sangre");
        checkFecha = new JCheckBox("Fecha");
        checkAltura = new JCheckBox("Altura");
        checkPeso = new JCheckBox("Peso");
        checkTelefono = new JCheckBox("Telefono");
        checkCiudad = new JCheckBox("Ciudad");
        checkAll = new JCheckBox("Todos");

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.add(panel1, Component.CENTER_ALIGNMENT);
        this.add(panel2, Component.CENTER_ALIGNMENT);
        this.add(panel3, Component.CENTER_ALIGNMENT);
        this.add(panel4, Component.CENTER_ALIGNMENT);

        text1.setPreferredSize(new Dimension(100, 30));
        text2.setPreferredSize(new Dimension(100, 30));
        text3.setPreferredSize(new Dimension(100, 30));
        list1.setBackground(Color.lightGray);
        scroll1.setViewportView(list1);

        panel1.setLayout(new FlowLayout());
        panel1.add(label1);
        panel1.add(text1);
        panel1.add(button1);
        panel1.add(button2);
        panel1.add(checkAll);

        panel2.setLayout(new FlowLayout());
        panel2.add(checkNombre);
        panel2.add(checkApellido);
        panel2.add(checkEmail);
        panel2.add(checkFecha);
        panel2.add(checkTipoSangre);
        panel2.add(checkAltura);
        panel2.add(checkPeso);
        panel2.add(checkTelefono);
        panel2.add(checkCiudad);

        panel3.setLayout(new BorderLayout());
        panel3.setBackground(Color.LIGHT_GRAY);
        panel3.add(scroll1, BorderLayout.CENTER);
        
        panel4.setLayout(new FlowLayout());
        panel4.add(label2);
        panel4.add(text2);
        panel4.add(label3);
        panel4.add(text3);
    }

    public static void main(String[] args) {
        GeneradorFrm generador = new GeneradorFrm();
        generador.setVisible(true);
        generador.setDefaultCloseOperation(EXIT_ON_CLOSE);
        generador.setSize(500, 400);
    }
}
