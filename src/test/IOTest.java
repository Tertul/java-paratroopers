package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IOTest {

    private String filename;

    @BeforeEach
    void setUp() throws FileNotFoundException {

        PrintWriter printer = new PrintWriter("stats_test.txt");
        printer.write("1\n");
        printer.write("22\n");
        printer.write("333\n");

        printer.close();
    }

    @Test
    void getStatsTest() throws IOException {
        filename = "stats_test.txt";
        List<String> lines = Files.readAllLines(Paths.get(filename));
        assertEquals(3, lines.size());
        assertEquals("1", lines.get(0));
        assertEquals("22", lines.get(1));
        assertEquals("333", lines.get(2));
    }

    @Test
    void InvalidFileTest() {
        try {
            filename = "NotARealTextFile.txt";
            //noinspection unused
            List<String> lines = Files.readAllLines(Paths.get(filename));
            fail("supposed to fail");
        } catch (IOException ioe) {
            System.out.println("caught ioe");
        }
    }

    @Test
    void ValidFileTest() {
        try{
            filename = "stats_test.txt";
            List<String> lines = Files.readAllLines(Paths.get(filename));
        }
        catch (IOException ioe){
            fail("not supposed to catch");
        }
    }

    @Test
    void writeStatsTest() throws IOException {
        filename = "stats_test.txt";
        List<String> lines = Files.readAllLines(Paths.get(filename));
        PrintWriter printer = new PrintWriter(filename);

        for (String line : lines)
            printer.write(line + "\n");

        assertEquals(3, lines.size());
        printer.write("4444\n");
        printer.close();

        lines = Files.readAllLines(Paths.get("stats_test.txt"));
        assertEquals(lines.get(3), "4444");
    }

}
