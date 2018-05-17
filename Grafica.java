import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

// Clase grafica (auxiliar).
public class Grafica {
    // Será una matriz de nxn y en la posición i,j  tendrá el peso de la arista que va del vertice i al vertice j.
    int[][] matriz;
    // Un arreglo de pobladores, un poblador es un arreglo de tamaño n (orden de la gráfica).
    int[][] poblacion = new int[10][];

    // Regresa el tamaño de la gráfica.
    public int getOrden() {
        return this.matriz[1].length;
    }

    // Constructor trivial.
    public Grafica() {
        this.matriz = new int[0][0];
    }

    // Recibe el orden de la gráfica.
    public Grafica(int n) {
        this.matriz = new int[n][n];
    }

    // Crea una gráfica de orden n y la llena con pesos aleatorios (entre 1 o 2);
    public void generaGrafica() {
        Random r = new Random();
        int n = getOrden();
        int peso;
        int limite = 30;
        // Itera la matriz y le asigna pesos.
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Generamos el random.
                peso = r.nextInt(limite) + 1;
                this.matriz[i][j] = peso;
                this.matriz[j][i] = peso;
            }
        }
    }

    public void imprimeGrafica() {
        int n = this.getOrden();
        System.out.println("Gráfica");
        // Itera la gráfica e imprime el valor de cada posición.
        System.out.print("    ");
        for (int i = 0; i < n; i++) {
            System.out.print("[V" + i + "]");
        }
        System.out.print("\n");
        for (int i = 0; i < n; i++) {
            System.out.print("[V" + i + "]");
            for (int j = 0; j < n; j++) {
                // Imprimimos
                // Para darle formato
                if (this.matriz[i][j] < 10) {
                    System.out.print("[0" + this.matriz[i][j] + "]");
                } else {
                    System.out.print("[" + this.matriz[i][j] + "]");
                }
            }
            System.out.print("\n");
        }
    }

    // Recibe un individuo y lo evalua.
    public double fitness(int[] individuo) {
        // Sumar todos los valores del arreglo que se le pasa.
        int total = 0;
        // Sumamos el primero con el último
        total = this.matriz[individuo[0]][individuo[individuo.length - 1]];
        for (int i = 0; i < individuo.length - 1; i++) {
            total += this.matriz[individuo[i]][individuo[i + 1]];
        }
        return total;
    }

    // Aplica el Ordered Crossover.
    public int[][] orderedCrossover(int[] padre, int[] madre) {
        // Ls hijos que dejará 
        int[] hijo_1 = new int[padre.length];
        int[] hijo_2 = new int[padre.length];
        // Pasamos la primera particíon a los hijos.
        for (int i = 10; i < padre.length - 11; i++) {
            // Hace el Cross :v 
            hijo_1[i] = madre[i];
            hijo_2[i] = padre[i];
        }
        int elemento_1;
        int elemento_2;
        // Primera parte del arreglo
        for (int i = 0; i < 10; i++) {
            // Para hijo 1
            for (int j = 0; j < padre.length; j++) {
                elemento_1 = padre[j];
                if (!contiene(hijo_1, elemento_1)) {
                    hijo_1[i] = elemento_1;
                    break;
                }
            }
            // Para el hijo 2
            for (int j = 0; j < padre.length; j++) {
                elemento_2 = madre[j];
                if (!contiene(hijo_2, elemento_2)) {
                    hijo_2[i] = elemento_2;
                    break;
                }
            }
        }
        // Segunda parte
        for (int i = padre.length - 11; i < padre.length; i++) {
            // Para el hijo 1
            for (int j = 0; j < padre.length; j++) {
                elemento_1 = padre[j];
                if (!contiene(hijo_1, elemento_1)) {
                    hijo_1[i] = elemento_1;
                    break;
                }
            }
            // Para el hijo 2
            for (int j = 0; j < padre.length; j++) {
                elemento_2 = madre[j];
                if (!contiene(hijo_2, elemento_2)) {
                    hijo_2[i] = elemento_2;
                    break;
                }
            }
        }
        int[][] hijos = new int[2][51];
        hijos[0] = hijo_1;
        hijos[1] = hijo_2;
        return hijos;
    }

    // Recibe a dos padres, para cruzarlos (deben tener la misma longitud).
    public int[][] partialCrossover(int[] padre, int[] madre) {
        // Ver que onda con los índices.
        // Ls hijos que dejará 
        int[] hijo_1 = new int[padre.length];
        int[] hijo_2 = new int[padre.length];
        // Provicional
        int[] aux_1 = new int[padre.length -19];
        int[] aux_2 = new int[padre.length -19];
        // Hace el primer paso
        for (int i = 9; i < padre.length -10; i++) {
            // Hace el Cross :v 
            hijo_1[i] = madre[i];
            hijo_2[i] = padre[i];
            aux_1[i - 9] = madre[i];
            aux_2[i - 9] = padre[i];
        }
        int elemento_1;
        int elemento_2;
        // Rellena los arreglos.
        // Primera parte del arreglo.
        for (int i = 0; i < 9; i++) {
            elemento_1 = padre[i];
            elemento_2 = madre[i];
            // Para el hijo 1
            if (contiene(hijo_1, elemento_1)) {
                for (int j = 0; j < aux_2.length; j++) {
                    if (aux_1[j] == elemento_1) {
                        elemento_1 = aux_2[j];
                        if (!contiene(hijo_1, elemento_1)) {
                            hijo_1[i] = elemento_1;
                            break;
                        } else {
                            j = -1;
                        }
                    }
                }
            } else {
                hijo_1[i] = elemento_1;
            }
            // Para el hijo 2
            if (contiene(hijo_2, elemento_2)) {
                for (int j = 0; j < aux_2.length; j++) {
                    if (aux_2[j] == elemento_2) {
                        elemento_2 = aux_1[j];
                        if (!contiene(hijo_2, elemento_2)) {
                            hijo_2[i] = elemento_2;
                            break;
                        } else {
                            j = -1;
                        }
                    }
                }
            } else {
                hijo_2[i] = elemento_2;
            }
        }
        // Segunda parte del arreglo
        for (int i = padre.length -10; i < padre.length; i++) {
            elemento_1 = padre[i];
            elemento_2 = madre[i];
            // Para el hijo 1
            if (contiene(hijo_1, elemento_1)) {
                for (int j = 0; j < aux_1.length; j++) {
                    if (aux_1[j] == elemento_1) {
                        elemento_1 = aux_2[j];
                        if (!contiene(hijo_1, elemento_1)) {
                            hijo_1[i] = elemento_1;
                            break;
                        } else {
                            // Para que empiece en 0 again.
                            j = -1;
                        }
                    }
                }
            } else {
                hijo_1[i] = elemento_1;
            }
            // Para el hijo 2
            if (contiene(hijo_2, elemento_2)) {
                for (int j = 0; j < aux_1.length; j++) {
                    if (aux_2[j] == elemento_2) {
                        elemento_2 = aux_1[j];
                        if (!contiene(hijo_2, elemento_2)) {
                            hijo_2[i] = elemento_2;
                            break;
                        } else {
                            j = -1;
                        }
                    }
                }
            } else {
                hijo_2[i] = elemento_2;
            }
        }
        int[][] hijos = new int[2][hijo_2.length];
        hijos[0] = hijo_1;
        hijos[1] = hijo_2;
        return hijos;
    }

    // Dice si un elemento está en el arreglo (individuo)
    public boolean contiene(int[] arreglo, int elemento) {
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i] == elemento) {
                return true;
            }
        }
        return false;
    }

    // Imprime un individuo
    public void imprimeIndividuo(int[] individuo) {
        System.out.print("(");
        for (int i = 0; i < individuo.length; i++) {
            System.out.print(individuo[i]);
            if ((i + 1) < individuo.length) {
                System.out.print(",");
            }
        }
        System.out.print(")\n");
    }

    // Aplica el Cycle Crossover.
    public void cycleCrossover(int[] padre, int[] madre) {
        // Ls hijos que dejará 
        int[] hijo_1 = new int[padre.length];
        int[] hijo_2 = new int[padre.length];
    }

    // Regresa 'true' con probabilidad 2
    // Para generar la probabilidad, hacemos un arreglo de diez elementos.
    // Dos elementos tendrán un valor especial y los otros ocho no.
    // Se genera un random entre 1 y 10. 
    // Si el arreglo en la posicion random tiene valor especial. regresa 'true'.
    // El valor especial es '1';
    public boolean tieneProbabilidad2() {
        int[] arreglo = { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 };
        Random r = new Random();
        int posicion = r.nextInt(10);
        if (arreglo[posicion] == 1) {
            // Si sí tuvo el valor especial.
            return true;
        }
        // Si no.
        return false;
    }

    // Para generar la probabilidad, hacemos un arreglo de diez elementos.
    // Dos elementos tendrán un valor especial y los otros ocho no.
    // Se genera un random entre 1 y 10. 
    // Si el arreglo en la posicion random tiene valor especial. regresa 'true'.
    // El valor especial es '1';
    public boolean tieneProbabilidad3() {
        int[] arreglo = { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 };
        Random r = new Random();
        int posicion = r.nextInt(10);
        if (arreglo[posicion] == 1) {
            // Si sí tuvo el valor especial.
            return true;
        }
        // Si no.
        return false;
    }

    // Ordena los elementos de mayor a menor;
    public void ordenaMayorAMenor(int[] arreglo) {
        int temp;
        for (int i = 0; i < arreglo.length; i++) {
            for (int j = i + 1; j < arreglo.length; j++) {
                if (arreglo[i] < arreglo[j]) {
                    temp = arreglo[i];
                    arreglo[i] = arreglo[j];
                    arreglo[j] = temp;
                }
            }
        }
    }

    // Ordena los elementos de menor a mayot;
    public void ordenaMenorAMayor(int[] arreglo) {
        int temp;
        for (int i = 0; i < arreglo.length; i++) {
            for (int j = i + 1; j < arreglo.length; j++) {
                if (arreglo[i] > arreglo[j]) {
                    temp = arreglo[i];
                    arreglo[i] = arreglo[j];
                    arreglo[j] = temp;
                }
            }
        }
    }

    // Genera la primera generación aleatoriamente.
    // Serán diez individuos.
    public int[][] llenaPoblacion() {
        int[][] poblacion = new int[10][51];
        Random r = new Random();
        int valor;
        // Para cada individuo.
        for (int i = 0; i < 10; i++) {
            // Para llenarlo.
            poblacion[i] = new int[51];
            for (int j = 0; j < 51; j++) {
                // Por que daba problema con el cero.
                poblacion[i][j] = -1;
                // Entre 0 y 8.
                valor = r.nextInt(51);
                // Para evitar repeticiones.
                while (contiene(poblacion[i], valor)) {
                    valor = r.nextInt(51);
                }
                poblacion[i][j] = valor;
            }
        }
        return poblacion;
    }

    // Ordena la población de menor a mayor respecto a su función fitness
    public void ordenaPoblacion(int[][] poblacion) {
        int[] aux = new int[9];
        for (int i = 0; i < poblacion.length; i++) {
            for (int j = i + 1; j < poblacion.length; j++) {
                if (fitness(poblacion[i]) > fitness(poblacion[j])) {
                    aux = poblacion[i];
                    poblacion[i] = poblacion[j];
                    poblacion[j] = aux;
                }
            }
        }
    }

    // Calcula el porcentaje de un valor, respecto al otro.
    public double porcentaje(double a, double b) {
        // El porcentaje de a respecto a b. 
        return round((a * 100 / b), 2);
    }

    public int imprimePoblacion(int[][] poblacion) {
        int costo_poblacion = 0;
        double fit;
        for (int[] individuo : poblacion) {
            imprimeIndividuo(individuo);
            fit = fitness(individuo);
            costo_poblacion += fit;
        }
        // System.out.println("Con costo de toda la población: " + costo_poblacion);
        return costo_poblacion;
    }

    // Para  no lididar con tantos números.
    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public int elijeCandidato(double[] valores) {
        int[] valores_entero = new int[valores.length];
        int total = 0;
        for (int i = 0; i < valores.length; i++) {
            total += (int) (valores[i]);
            valores_entero[i] = (int) (valores[i]);
            // System.out.println("Ciclo 1");
        }
        Random r = new Random();
        int aleatorio = r.nextInt(total) + 1;
        int contador = 0;
        int indice = 0;
        for (int i = 0; i < valores.length; i++) {
            contador += valores[i];
            if (aleatorio <= contador) {
                indice = i;
                break;
            }
            // System.out.println("Ciclo 2");
        }
        return indice;
    }

    // Aplica la mutación "Displacement mutation". 
    public void displacementMutation(int[] individuo) {
        // Tamaño por definir.
        int[] aux = new int[10];
        for (int i = 2; i < aux.length + 2; i++) {
            aux[i - 2] = individuo[i];
        }
        for (int i = 2; i < 11; i++) {
            individuo[i] = individuo[i + aux.length];
        }
        // desplazamineto
        for (int i = 0; i < aux.length; i++) {
            individuo[aux.length + i] = aux[i];
        }
    }

    // Aplica la mutación "Exchange mutation".
    public void exchangeMutation(int[] individuo) {
        Random r = new Random();
        int i_1 = r.nextInt(individuo.length);
        int i_2 = r.nextInt(individuo.length);
        while (i_2 == i_1) {
            i_2 = r.nextInt(individuo.length);
        }
        int aux = individuo[i_1];
        individuo[i_1] = individuo[i_2];
        individuo[i_2] = aux;
    }

    // Calcula las probabilidades de cada individuo a ser eliminado
    public void calculaProbabilidades(double[] probabilidades, double[] probabilidades_malas, int[][] poblacion) {
        double costo_total = 0;
        for (int[] individuo : poblacion) {
            costo_total += fitness(individuo);
        }
        double porcentaje;
        double fitness;
        for (int i = 0; i < poblacion.length; i++) {
            // Para porcentaje malo.
            fitness = fitness(poblacion[i]);
            porcentaje = porcentaje(fitness, costo_total);
            probabilidades_malas[i] = porcentaje;
            // Para porcentaje bueno.
            fitness = fitness(poblacion[poblacion.length - i - 1]);
            porcentaje = porcentaje(fitness, costo_total);
            probabilidades[i] = porcentaje;
        }
    }

    // Toda la ejecución del programa.
    public void principal() {
        // Para hacer mediciones
        double peor_fitness = 0;
        double mejor_fitness = 0;
        // Generamos la población inicial.
        // Un conjunto(arreglo) de cardinalidad 10 de individuos (arreglos) de tamaño 9.
        int[][] poblacion = llenaPoblacion();
        double fitness = 0;
        double costo_total = 0;
        mejor_fitness = Integer.MAX_VALUE;
        // Para hacer cálculos.
        for (int[] individuo : poblacion) {
            fitness = fitness(individuo);
            costo_total += fitness;
            if (mejor_fitness > fitness) {
                mejor_fitness = fitness;
            }
            // Para pruebas
            if (peor_fitness < fitness) {
                peor_fitness = fitness;
            }
        }
        System.out.println("El mejor fitness:  " + mejor_fitness);
        System.out.println("El peor fitness: " + peor_fitness);
        ordenaPoblacion(poblacion);
        imprimePoblacion(poblacion);
        double fitness_auxiliar;
        // Para guardar las probabilidades a ser elegidos/eliminados
        double[] probabilidades_malas = new double[poblacion.length];
        double[] probabilidades = new double[poblacion.length];
        calculaProbabilidades(probabilidades, probabilidades_malas, poblacion);
        // double fitness_auxiliar;
        // Para hacer la horchata
        int generacion = 0;
        int i1 , i2;
        int iteraciones_sin_cambio = 0;
        int costo_poblacion = 0;
        // Para elegir  que cruce usar.
        Random r = new Random();
        int random;
        // Para los hijos que dará el cruce.
        int[][] hijos;
        int mutaciones = 0;
        while (generacion <= 500) {
            i1 = elijeCandidato(probabilidades);
            i2 = elijeCandidato(probabilidades);
            // Para no cruzar el mismo hommie.
            while (i1 == i2) {
                i2 = elijeCandidato(probabilidades);
            }
            // Para ver como cruzarlos
            random = r.nextInt(2);
            if (random == 1) {
                hijos = orderedCrossover(poblacion[i1], poblacion[i2]);
            } else {
                hijos = partialCrossover(poblacion[i1], poblacion[i2]);
            }
            // Para aplicar mutación 
            random = r.nextInt(10);
            // Probabilidad .2
            if (random < 2) {
                mutaciones++;
                if (random == 0) {
                    displacementMutation(hijos[0]);
                } else {
                    exchangeMutation(hijos[0]);
                }
            }
            random = r.nextInt(10);
            if (random < 2) {
                mutaciones++;
                if (random == 0) { 
                    displacementMutation(hijos[1]);
                } else {
                    exchangeMutation(hijos[1]);
                }
            }
            // Eliminamos y sustituimos.
            i1 = elijeCandidato(probabilidades_malas);
            i2 = elijeCandidato(probabilidades_malas);
            poblacion[i1] = hijos[0];
            poblacion[i2] = hijos[1];
            generacion++;
            ordenaPoblacion(poblacion);
            calculaProbabilidades(probabilidades, probabilidades_malas, poblacion);
        }
        mejor_fitness = Integer.MAX_VALUE;
        peor_fitness = 0;
        // Para ver cambios
        for (int i = 0; i < poblacion.length; i++) {
            // g.imprimeIndividuo(poblacion[i]);
            fitness = fitness(poblacion[i]);
            if (fitness < mejor_fitness) {
                mejor_fitness = fitness;
            }
            if (peor_fitness < fitness) {
                peor_fitness = fitness;
            }
        }
        System.out.println("El mejor fitness ahora: " + mejor_fitness);
        System.out.println("El peor fitness ahora: " + peor_fitness);
        System.out.println("Se aplicaron :" + mutaciones + " mutaciones");
        imprimePoblacion(poblacion);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Grafica g = new Grafica(51);
        try {
            BufferedReader in = new BufferedReader(new FileReader("ciudades.txt"));
            String line;
            char [] valores;
            String aux;
            String [] ciudades = null;
            int i = 1;
            while((line = in.readLine()) != null) {
                line = line.trim();
                ciudades = line.split("\t");
                for (int j = 0;j<ciudades.length ;j++ ) {
                    g.matriz[i][j] = Integer.parseInt(ciudades[j]);
                    g.matriz[j][i] = Integer.parseInt(ciudades[j]);
                }
                i++;
                // System.out.print("\n");
            }
            g.imprimeGrafica();
            g.principal();
            in.close(); 
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}


