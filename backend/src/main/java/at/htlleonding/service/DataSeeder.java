package at.htlleonding.service;

import at.htlleonding.model.*;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class DataSeeder {

    @Inject
    EntityManager em;

    @Inject
    PasswordService passwordService;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        // Nur seeden, wenn die DB leer ist (verhindert Duplikate in Prod)
        Long count = em.createQuery("SELECT COUNT(q) FROM Question q", Long.class).getSingleResult();
        if (count > 0) return;

        try {
            // 1. Fragen anlegen
            Map<Integer, Question> questions = seedQuestions();

            // 2. Player anlegen (mit Password-Hashing)
            Map<String, Player> players = seedPlayers();

            // 3. Gruppen anlegen
            Map<Integer, EntityGroup> groups = seedGroups();

            // 4. Player zu Gruppen hinzufügen (Player_Group)
            seedPlayerGroupRelations(players, groups);

            // 5. Aktive Gruppen-Fragen (Games) starten
            seedGroupQuestions(questions, groups, players);

            System.out.println("✅ Database Seeding erfolgreich abgeschlossen!");
        } catch (Exception e) {
            System.err.println("❌ Seeding Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Map<Integer, Question> seedQuestions() {
        Map<String, String> qData = new LinkedHashMap<>();
        qData.put("Wer würde am ehesten versuchen in einer Fremdsprache zu kommunizieren, indem er einfach deutsche Wörter mit einem Akzent ausspricht?", "In Fremdsprache mit Akzent sprechen");
        qData.put("Wer von uns würde am wenigsten das Haus verlassen, wenn der Handyakku fast leer ist, und deshalb lieber zu spät kommen?", "Zu spät wegen leerem Handy");
        qData.put("Wer lebt am stärksten nach dem Motto \"Man lebt nur einmal im Leben\"?", "YOLO leben");
        qData.put("Wer würde eine gesamte Gruppenreise dokumentieren, aber die Fotos nie mit uns teilen?", "Fotos nicht teilen");
        qData.put("Wer überreagiert am schnellsten?", "Schnell überreagieren");
        qData.put("Jeder in der Gruppe zeigt dir einen Song. Wer hat den unvorhersehbarsten Musikgeschmack?", "Unvorhersehbarer Musikgeschmack");
        qData.put("Wer würde es am kürzesten in einer dreckigen Wohnung aushalten? Putzen ist nicht erlaubt!", "Dreckige Wohnung aushalten");
        qData.put("Wer ist am schnellsten Teil von neuen Trends?", "Neue Trends mitmachen");
        qData.put("Wer würde am ehesten kriminell werden, um einem Freund bei etwas zu helfen?", "Für Freund kriminell werden");
        qData.put("Wer würde am ehesten versuchen, sich mit Wortwitz und Charme aus einem Strafzettel herauszureden?", "Aus Strafzettel herausreden");
        qData.put("Wer könnte die beste TikTok-Tanzperformance hinlegen?", "TikTok-Tanz performen");
        qData.put("Wen würde man öffentlich am wenigsten als \"politisch korrekt\" einstufen?", "Nicht politisch korrekt");
        qData.put("Ganz alleine in einem leeren Raum: Wer würde am schnellsten die Nerven verlieren?", "Alleine Nerven verlieren");

        Map<Integer, Question> result = new HashMap<>();
        int i = 1;
        for (Map.Entry<String, String> entry : qData.entrySet()) {
            Question q = new Question();
            q.setQuestion(entry.getKey());
            q.setShortenedQuestion(entry.getValue());
            em.persist(q);
            result.put(i++, q);
        }
        return result;
    }

    private Map<String, Player> seedPlayers() throws Exception {
        List<String> names = List.of("Isabella", "Max", "Herbert", "Frederike", "Gertrude", "Thomas", "Kinga", "Timon", "Lien", "Stefanie", "Christoph", "Tobi", "Timmy", "Georgina", "Julian", "Anne", "Peter", "Bob", "TestUser1", "TestUser2");
        Map<String, Player> result = new HashMap<>();
        String sha256Pass = hashToSha256("testpass123");
        String bcryptHash = passwordService.hashPassword(sha256Pass);

        for (String name : names) {
            Player p = new Player(name);
            p.setPassword(bcryptHash);
            em.persist(p);
            result.put(name, p);
        }
        return result;
    }

    private Map<Integer, EntityGroup> seedGroups() {
        String[][] gData = {
                {"Familie", "1"},
                {"4AHITM", "2"},
                {"Die 5 Freunde", "3"},
                {"Die 3 ???", "4"},
                {"Google Test Group", "5"}
        };

        Map<Integer, EntityGroup> result = new HashMap<>();
        for (String[] g : gData) {
            // Leeren Konstruktor nutzen
            EntityGroup group = new EntityGroup();

            // Felder einzeln setzen
            group.setName(g[0]);
            group.setLink("https://taskosaurus.at/group/" + g[1]);

            em.persist(group);

            // In die Map speichern, damit wir später die Player zuordnen können
            result.put(Integer.parseInt(g[1]), group);
        }
        return result;
    }

    private void seedPlayerGroupRelations(Map<String, Player> p, Map<Integer, EntityGroup> g) {
        // Familie (Group 1)
        addPToG(p.get("Isabella"), g.get(1)); addPToG(p.get("Max"), g.get(1)); addPToG(p.get("Herbert"), g.get(1)); addPToG(p.get("Frederike"), g.get(1)); addPToG(p.get("Gertrude"), g.get(1));
        // 4AHITM (Group 2)
        addPToG(p.get("Isabella"), g.get(2)); addPToG(p.get("Thomas"), g.get(2)); addPToG(p.get("Kinga"), g.get(2)); addPToG(p.get("Timon"), g.get(2)); addPToG(p.get("Lien"), g.get(2)); addPToG(p.get("Stefanie"), g.get(2)); addPToG(p.get("Christoph"), g.get(2)); addPToG(p.get("Tobi"), g.get(2));
        // Test Group (Group 5)
        addPToG(p.get("TestUser1"), g.get(5)); addPToG(p.get("TestUser2"), g.get(5));
    }

    private void addPToG(Player p, EntityGroup g) {
        if (p == null || g == null) return;
        em.createNativeQuery("INSERT INTO Player_Group (player_id, group_id) VALUES (?, ?)")
                .setParameter(1, p.getId()).setParameter(2, g.getId()).executeUpdate();
    }

    private void seedGroupQuestions(Map<Integer, Question> q, Map<Integer, EntityGroup> g, Map<String, Player> p) {
        // Spiel in Gruppe 5 (Google Test) - Frage 1
        GroupQuestion gq = new GroupQuestion();
        gq.setQuestion(q.get(1));
        gq.setGroup(g.get(5));
        gq.setDate(LocalDate.now());
        em.persist(gq);

        // Antwort von TestUser2 für TestUser1
        GroupQuestionAnswer answer = new GroupQuestionAnswer();
        answer.setAnsweringPlayer(p.get("TestUser2"));
        answer.setAnswer(p.get("TestUser1"));
        answer.setGroupQuestion(gq);
        em.persist(answer);
    }

    private String hashToSha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}