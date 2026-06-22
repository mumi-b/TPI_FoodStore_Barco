package integrado.prog2.ui;

import java.util.Scanner;

/**
 * Utilidades para leer datos de la consola de forma segura.
 * Centraliza el Scanner para que los menus no se llenen de validaciones repetidas.
 *
 * @author Eugenia
 */
public class ConsolaUtil {

    // Un unico Scanner para toda la aplicacion (sobre System.in).
    private static final Scanner sc = new Scanner(System.in);

    // Lee un texto cualquiera (puede quedar vacio).
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    // Lee un texto obligatorio: vuelve a pedir hasta que no este vacio.
    public static String leerTextoObligatorio(String mensaje) {
        String texto;
        do {
            texto = leerTexto(mensaje);
            if (texto.isBlank()) {
                System.out.println("(!) Este dato es obligatorio. Intente de nuevo.");
            }
        } while (texto.isBlank());
        return texto;
    }

    // Lee un entero. Si el usuario escribe cualquier cosa que no sea numero, vuelve a pedir.
    public static int leerEntero(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje);
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("(!) Debe ingresar un numero entero valido.");
            }
        }
    }

    // Lee un Long (para los ids).
    public static Long leerLong(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje);
            try {
                return Long.parseLong(entrada);
            } catch (NumberFormatException e) {
                System.out.println("(!) Debe ingresar un id numerico valido.");
            }
        }
    }

    // Lee un numero decimal (para precios).
    public static double leerDouble(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje);
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("(!) Debe ingresar un numero valido (ej: 1500.50).");
            }
        }
    }

    // Pregunta si/no y devuelve true si la respuesta es S.
    public static boolean leerSiNo(String mensaje) {
        String r = leerTexto(mensaje + " (S/N): ");
        return r.equalsIgnoreCase("S");
    }

    // Pausa hasta que el usuario apriete Enter.
    public static void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        sc.nextLine();
    }
}
