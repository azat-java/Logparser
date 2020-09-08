package logparser;

import logparser.exceptions.NoSuchFileException;
import logparser.exceptions.NotValidFileSizeException;
import logparser.model.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    final static String REG_LOGS = "([0-9-:. ]{23}) \\[(\\w*-?\\w*)] (INFO|DEBUG|ERROR|FATAL) (.*)";
    final static Integer MAX_FILE_SIZE_MB = 10;

    public static void main(String[] args) {
        System.out.println("Program of parsing logfile to database\n");
        try {
            // use "data/logs_example.csv" as example
            Path logsFilePath = Paths.get(args[0]);
            if (!isFileCorrect(logsFilePath)) {
                return;
            }

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
            Session session = sessionFactory.openSession();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            addAllRecords(logsFilePath, session, sdf);
            sessionFactory.close();

        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    // adding record of log in database
    static void addRecord(Session session, SimpleDateFormat sdf, Matcher matcher) throws ParseException {
        if (matcher.matches()) {
            Log log = new Log(sdf.parse(matcher.group(1)), matcher.group(2), matcher.group(3), matcher.group(4));
            session.save(log);
        }
    }

    // checking file size
    static boolean isFileCorrect(Path logsFilePath) throws IOException {
        if (!new File(String.valueOf(logsFilePath)).isFile()) {
            throw new NoSuchFileException();
        } else if (Files.size(logsFilePath) / Math.pow(2, 20) > MAX_FILE_SIZE_MB) {
            throw new NotValidFileSizeException(MAX_FILE_SIZE_MB);
        }
        return true;
    }

    // writing all records of logs in database in transaction
    static void addAllRecords(Path logsFilePath, Session session, SimpleDateFormat sdf) throws IOException, ParseException {
        Transaction transaction = session.beginTransaction();
        for (String line : Files.readAllLines(logsFilePath)) {
            addRecord(session, sdf, Pattern.compile(REG_LOGS).matcher(line));
        }
        transaction.commit();
    }
}