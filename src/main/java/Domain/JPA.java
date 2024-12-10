package Domain;

import Fabrica.FabricaDeModelo;
import Marca.Modelo;
import Representante.Vendas;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.w3c.dom.Document;

import java.util.Arrays;
import java.util.List;

public class JPA {

    public static void main(String[] args) {
        // Conexão com PostgreSQL (JPA)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ebac");
        EntityManager em = emf.createEntityManager();

        // Conexão com MongoDB
        String mongoUri = "mongodb://localhost:27017";
        String databaseName = "carros";
        MongoClientFactory mongoClient = MongoClientFactory.create(mongoUri);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = mongoDatabase.getCollection("vendas");

        em.getTransaction().begin();

        // Persistir dados no PostgreSQL
        FabricaDeModelo fabrica1 = new FabricaDeModelo();
        fabrica1.setAnoDeFabricacao(2020);
        fabrica1.setMarca("Toyota");
        em.persist(fabrica1);

        Modelo modelo1 = new Modelo();
        modelo1.setModeloDoCarro("Corolla");
        modelo1.setAnoDeFabricacao(2020);
        modelo1.setMarca("Toyota");
        modelo1.setPreco(90000);
        modelo1.setFabricaDeModelo(fabrica1);
        em.persist(modelo1);

        Vendas venda1 = new Vendas();
        venda1.setAnoDeFabricacao(2020);
        venda1.setModelo("Corolla");
        venda1.setMarca("Toyota");
        venda1.setValorDeVenda(93000);
        venda1.setComissao(3000);
        venda1.setModelosVendidos(Arrays.asList(modelo1));
        em.persist(venda1);

        em.getTransaction().commit();

        // Persistir dados no MongoDB
        Document vendaDocument = new Document()
                .append("anoDeFabricacao", venda1.getAnoDeFabricacao())
                .append("modelo", venda1.getModelo())
                .append("marca", venda1.getMarca())
                .append("valorDeVenda", venda1.getValorDeVenda())
                .append("comissao", venda1.getComissao())
                .append("modelosVendidos", Arrays.asList(venda1.getModelo()));

        collection.insertOne(vendaDocument);

        // Consultas em PostgreSQL
        consultarModelosPorAno(em, 2020);
        consultarModelosPorPreco(em, 90000);

        // Consulta em MongoDB
        consultarVendasMongoDB(collection);

        // Fechar conexões
        em.close();
        emf.close();
        mongoClient.finalize();

        System.out.println("Dados inseridos e consultados com sucesso!");
    }

    private static void consultarModelosPorAno(EntityManager em, int ano) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Modelo> cq = cb.createQuery(Modelo.class);
        Root<Modelo> root = cq.from(Modelo.class);

        cq.select(root).where(cb.equal(root.get("anoDeFabricacao"), ano));
        List<Modelo> modelos = em.createQuery(cq).getResultList();

        System.out.println("Modelos fabricados no ano " + ano + ":");
        modelos.forEach(m -> System.out.println(m.getModeloDoCarro()));
    }

    private static void consultarModelosPorPreco(EntityManager em, double preco) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Modelo> cq = cb.createQuery(Modelo.class);
        Root<Modelo> root = cq.from(Modelo.class);

        cq.select(root).where(cb.greaterThan(root.get("preco"), preco));
        List<Modelo> modelos = em.createQuery(cq).getResultList();

        System.out.println("Modelos com preço maior que " + preco + ":");
        modelos.forEach(m -> System.out.println(m.getModeloDoCarro()));
    }

    private static void consultarVendasMongoDB(MongoCollection<Document> collection) {
        System.out.println("Vendas no MongoDB:");
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
    }
}
