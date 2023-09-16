import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class DequeCity implements Iterable<Cidade> {
    private int n;
    private No sentinela;

    public DequeCity() {
        n = 0;
        sentinela = new No();
        sentinela.prox = sentinela;
        sentinela.ant = sentinela;
    }

    private class No {
        private Cidade dado;
        private String chave;
        private No prox;
        private No ant;
    }

    public void pushFront(String key, Cidade item) {
        No tmp = new No();
        tmp.dado = item;
        tmp.chave = key;

        tmp.ant = sentinela;
        tmp.prox = sentinela.prox;

        sentinela.prox = tmp;
        tmp.prox.ant = tmp;
        ++n;
    }

    public void pushBack(String key, Cidade item) {
        No tmp = new No();
        tmp.dado = item;
        tmp.chave = key;

        tmp.ant = sentinela.ant;
        tmp.prox = sentinela;

        sentinela.ant = tmp;
        tmp.ant.prox = tmp;
        ++n;
    }

    public boolean contains(String key) {
        if(key == null)
            throw new IllegalArgumentException("Argument to contains() is null");
        return get(key) != null;
    }

    public Cidade get(String key) {
        if(key == null)
            throw new IllegalArgumentException("Argument to get() is null");
        for(No x = sentinela.prox; x != sentinela; x = x.prox)
            if(key.equals(x.chave))
                return x.dado;
        return null;
    }

    public void delete(String key) {
        if(key == null)
            throw new IllegalArgumentException("Argument to delete() is null");
        delete(sentinela.prox, key);
    }

    private void remove(No tmp) {
        tmp.ant.prox = tmp.prox;
        tmp.prox.ant = tmp.ant;
        --n;
    }

    private void delete(No x, String key) {
        if(x == sentinela) return;
        if(key.equals(x.chave)) {
            remove(x);
            return;
        }
        delete(x.prox, key);
    }

    public void put(String key, Cidade val) {
        if(key == null)
            throw new IllegalArgumentException("First argument to put() is null");
        if(val == null) {
            delete(key);
            return;
        }
        for(No x = sentinela.prox; x != sentinela; x = x.prox) {
            if(key.equals(x.chave)) {
                x.dado = val;
                return;
            }
        }
        pushFront(key, val);
    }

    public Cidade popFront() {
        No tmp = sentinela.prox;
        Cidade meuDado = tmp.dado;
        tmp.ant.prox = tmp.prox;
        tmp.prox.ant = tmp.ant;
        --n;
        return meuDado;
    }

    public Cidade popBack() {
        No tmp = sentinela.ant;
        Cidade meuDado = tmp.dado;
        tmp.ant.prox = tmp.prox;
        tmp.prox.ant = tmp.ant;
        --n;
        return meuDado;
    }

    public No first() {
        if(sentinela == sentinela.prox) return null;
        return sentinela.prox;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    @Override
    public ListIterator<Cidade> iterator() {
        return new DequeIterator();
    }

    public class DequeIterator implements ListIterator<Cidade> {
        private No atual = sentinela.prox;
        private int indice = 0;
        private No acessadoUltimo = null;

        public boolean hasNext() { return indice < (n); }
        public boolean hasPrevious() { return indice > 0; }
        public int previousIndex() { return indice - 1; }
        public int nextIndex() { return indice; }

        public Cidade next() {
            if(!hasNext()) return null;

            Cidade meuDado = atual.dado;
            acessadoUltimo = atual;
            atual = atual.prox;
            indice++;
            return meuDado;
        }

        public Cidade previous() {
            if(!hasPrevious()) return null;
            atual = atual.ant;

            Cidade meuDado = atual.dado;
            acessadoUltimo = atual;
            indice--;
            return meuDado;
        }

        public Cidade get() {
            if(atual == null) throw new IllegalStateException();
            return atual.dado;
        }

        public void set(Cidade x) {
            if(acessadoUltimo == null) throw new IllegalStateException();
            acessadoUltimo.dado = x;
        }

        public void remove() {
            if(acessadoUltimo == null)
                throw new IllegalStateException();
            acessadoUltimo.ant.prox = acessadoUltimo.prox;
            acessadoUltimo.prox.ant = acessadoUltimo.ant;
            --n;
            if(atual == acessadoUltimo) atual = acessadoUltimo.prox;
            else indice--;
            acessadoUltimo = null;
        }

        public void add(Cidade x) {
            No tmp = new No();
            tmp.dado = x;

            tmp.prox = atual.prox;
            tmp.ant = atual;

            tmp.prox.ant = tmp;
            atual.prox = tmp;
            n++;
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for(Cidade item : this) s.append(item + " ");
        return s.toString();
    }

    public Iterable<String> keys() {
        Deque<String> queue = new ArrayDeque<>();
        for(No x = sentinela.prox; x != sentinela; x = x.prox)
            queue.push(x.chave);
        return queue;
    }

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("\n\nUso: java DequeCity arquivo-1 arquivo-2\n\n");
            System.exit(0);
        }
        try {
            FileReader in1 = new FileReader(args[0]);
            BufferedReader br = new BufferedReader(in1);
            int total = Integer.parseInt(br.readLine());
            int temperature = 0;
            System.out.println(" Total = " + total);
            DequeCity st = new DequeCity();

            for(int i = 0; i < total; i++) {
                String tmp = br.readLine();
                StringTokenizer tk = new StringTokenizer(tmp);
                String key = tk.nextToken();
                temperature = Integer.parseInt(tk.nextToken());
                Cidade myCity = new Cidade(key, temperature);
                st.put(key, myCity);
            }
            br.close();
            in1.close();
            System.out.println("-----Testando---- Procure afterword");
            System.out.println(st.get("afterword"));
            System.out.println("-----Testando---- Procure Feeney");
            System.out.println(st.get("Feeney"));
            System.out.println("-----Testando---- Procure Fee");
            System.out.println(st.get("Fee"));

            in1 = new FileReader(args[1]);
            br = new BufferedReader(in1);

            total = Integer.parseInt(br.readLine());
            for(int i = 0; i < total; i++) {
                String tmp = br.readLine();
                StringTokenizer tk = new StringTokenizer(tmp);
                Cidade myCity = st.get(tk.nextToken());

                if(myCity == null) System.out.println("\n[Failed] " + tmp + " não foi encontrada.");
                else {
                    System.out.println("\n[Ok] " + myCity.getNome() + " foi encontrada. Temperatura lá é: " + myCity.getTemp() + "Fº");
                }
            }
            br.close();
            in1.close();
        } catch(IOException e) {
            
        }
    }
}