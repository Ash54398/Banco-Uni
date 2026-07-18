/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clase;

/**
 *
 * @author Hugo Cabello
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Banco {
 
    // ------------------- CLASE CUENTA -------------------
    static class Cuenta {
        private final String numeroCuenta;
        private double saldo;
        private final List<String> historial = new ArrayList<>();
 
        public Cuenta(String numeroCuenta, double saldoInicial) {
            this.numeroCuenta = numeroCuenta;
            this.saldo = saldoInicial;
            registrar("Cuenta creada con saldo inicial: " + saldoInicial);
        }
 
        public String getNumeroCuenta() {
            return numeroCuenta;
        }
 
        public double getSaldo() {
            return saldo;
        }
 
        public void depositar(double monto) {
            if (monto <= 0) {
                throw new IllegalArgumentException("El monto a depositar debe ser positivo.");
            }
            saldo += monto;
            registrar("Depósito: +" + monto + " | Saldo actual: " + saldo);
        }
 
        public void retirar(double monto) {
            if (monto <= 0) {
                throw new IllegalArgumentException("El monto a retirar debe ser positivo.");
            }
            if (monto > saldo) {
                throw new IllegalStateException("Fondos insuficientes en la cuenta " + numeroCuenta);
            }
            saldo -= monto;
            registrar("Retiro: -" + monto + " | Saldo actual: " + saldo);
        }
 
        private void registrar(String movimiento) {
            historial.add(movimiento);
        }
 
        public void mostrarHistorial() {
            System.out.println("Historial de la cuenta " + numeroCuenta + ":");
            for (String h : historial) {
                System.out.println("  - " + h);
            }
        }
    }
 
    // ------------------- CLASE CLIENTE -------------------
    static class Cliente {
        private final String nombre;
        private final String id;
        private final List<Cuenta> cuentas = new ArrayList<>();
 
        public Cliente(String nombre, String id) {
            this.nombre = nombre;
            this.id = id;
        }
 
        public String getNombre() {
            return nombre;
        }
 
        public String getId() {
            return id;
        }
 
        public void agregarCuenta(Cuenta cuenta) {
            cuentas.add(cuenta);
        }
 
        public List<Cuenta> getCuentas() {
            return cuentas;
        }
    }
 
    // ------------------- CLASE SISTEMA BANCARIO -------------------
    static class SistemaBancario {
        private final String nombreBanco;
        private final Map<String, Cliente> clientes = new HashMap<>();
        private final Map<String, Cuenta> cuentas = new HashMap<>();
        private int contadorCuentas = 1000;
 
        public SistemaBancario(String nombreBanco) {
            this.nombreBanco = nombreBanco;
        }
 
        public Cliente registrarCliente(String nombre, String id) {
            if (clientes.containsKey(id)) {
                throw new IllegalArgumentException("Ya existe un cliente con el ID " + id);
            }
            Cliente cliente = new Cliente(nombre, id);
            clientes.put(id, cliente);
            return cliente;
        }
 
        public Cuenta abrirCuenta(String idCliente, double saldoInicial) {
            Cliente cliente = clientes.get(idCliente);
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente no encontrado: " + idCliente);
            }
            String numeroCuenta = "CTA-" + (contadorCuentas++);
            Cuenta cuenta = new Cuenta(numeroCuenta, saldoInicial);
            cliente.agregarCuenta(cuenta);
            cuentas.put(numeroCuenta, cuenta);
            return cuenta;
        }
 
        public Cuenta buscarCuenta(String numeroCuenta) {
            Cuenta cuenta = cuentas.get(numeroCuenta);
            if (cuenta == null) {
                throw new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta);
            }
            return cuenta;
        }
 
        public Cliente buscarCliente(String idCliente) {
            Cliente cliente = clientes.get(idCliente);
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente no encontrado: " + idCliente);
            }
            return cliente;
        }
 
        public void transferir(Cuenta origen, Cuenta destino, double monto) {
            origen.retirar(monto);
            destino.depositar(monto);
        }
 
        public void mostrarResumenCliente(String idCliente) {
            Cliente cliente = buscarCliente(idCliente);
            System.out.println("\nResumen de " + cliente.getNombre() + ":");
            double total = 0;
            for (Cuenta c : cliente.getCuentas()) {
                System.out.println("  Cuenta " + c.getNumeroCuenta() + " -> Saldo: " + c.getSaldo());
                total += c.getSaldo();
            }
            System.out.println("  Total en " + nombreBanco + ": " + total);
        }
    }
 
    // ------------------- MENU INTERACTIVO -------------------
    public static void main(String[] args) {
        SistemaBancario banco = new SistemaBancario("Banco Java S.A.");
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
 
        System.out.println("=== BIENVENIDO A " + "Banco Java S.A." + " ===");
 
        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();
 
            try {
                switch (opcion) {
                    case "1": { // Registrar cliente
                        System.out.print("Nombre del cliente: ");
                        String nombre = sc.nextLine();
                        System.out.print("ID del cliente (ej. C001): ");
                        String id = sc.nextLine();
                        banco.registrarCliente(nombre, id);
                        System.out.println("Cliente registrado correctamente.");
                        break;
                    }
                    case "2": { // Abrir cuenta
                        System.out.print("ID del cliente: ");
                        String idCliente = sc.nextLine();
                        System.out.print("Saldo inicial: ");
                        double saldoInicial = Double.parseDouble(sc.nextLine());
                        Cuenta nueva = banco.abrirCuenta(idCliente, saldoInicial);
                        System.out.println("Cuenta creada: " + nueva.getNumeroCuenta());
                        break;
                    }
                    case "3": { // Depositar
                        System.out.print("Numero de cuenta: ");
                        String numCuenta = sc.nextLine();
                        System.out.print("Monto a depositar: ");
                        double monto = Double.parseDouble(sc.nextLine());
                        Cuenta cuenta = banco.buscarCuenta(numCuenta);
                        cuenta.depositar(monto);
                        System.out.println("Deposito exitoso. Nuevo saldo: " + cuenta.getSaldo());
                        break;
                    }
                    case "4": { // Retirar
                        System.out.print("Numero de cuenta: ");
                        String numCuenta = sc.nextLine();
                        System.out.print("Monto a retirar: ");
                        double monto = Double.parseDouble(sc.nextLine());
                        Cuenta cuenta = banco.buscarCuenta(numCuenta);
                        cuenta.retirar(monto);
                        System.out.println("Retiro exitoso. Nuevo saldo: " + cuenta.getSaldo());
                        break;
                    }
                    case "5": { // Transferir
                        System.out.print("Cuenta de origen: ");
                        String origenNum = sc.nextLine();
                        System.out.print("Cuenta de destino: ");
                        String destinoNum = sc.nextLine();
                        System.out.print("Monto a transferir: ");
                        double monto = Double.parseDouble(sc.nextLine());
                        Cuenta origen = banco.buscarCuenta(origenNum);
                        Cuenta destino = banco.buscarCuenta(destinoNum);
                        banco.transferir(origen, destino, monto);
                        System.out.println("Transferencia realizada con éxito.");
                        break;
                    }
                    case "6": { // Ver resumen del cliente
                        System.out.print("ID del cliente: ");
                        String idCliente = sc.nextLine();
                        banco.mostrarResumenCliente(idCliente);
                        break;
                    }
                    case "7": { // Ver historial de una cuenta
                        System.out.print("Número de cuenta: ");
                        String numCuenta = sc.nextLine();
                        Cuenta cuenta = banco.buscarCuenta(numCuenta);
                        cuenta.mostrarHistorial();
                        break;
                    }
                    case "8": // Salir
                        salir = true;
                        System.out.println("Gracias por usar el banco. ¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción no válida, intenta de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: debes ingresar un número válido.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
 
        sc.close();
    }
 
    private static void mostrarMenu() {
        System.out.println("\n----- MENU -----");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Abrir cuenta");
        System.out.println("3. Depositar");
        System.out.println("4. Retirar");
        System.out.println("5. Transferir entre cuentas");
        System.out.println("6. Ver resumen de cliente");
        System.out.println("7. Ver historial de una cuenta");
        System.out.println("8. Salir");
        System.out.print("Elige una opcion: ");
    }
}
        
        
        
    



        
   
 








    

   