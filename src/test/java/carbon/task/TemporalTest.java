package carbon.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TemporalTest {
    @Test
    public void parse_validDate_success() {
        assertEquals("1 Jul 2024", Temporal.parse("1 Jul 2024").toString());
        assertEquals("1 Jul 2024", Temporal.parse("01-07-2024").toString());
        assertEquals("1 Jul 2024", Temporal.parse("1-7-2024").toString());
        assertEquals("1 Jul 2024", Temporal.parse("2024-07-01").toString());
        assertEquals("1 Jul 2024", Temporal.parse("2024-7-1").toString());
    }

    @Test
    public void parse_validTime_success() {
        assertEquals("1:00pm", Temporal.parse("1:00pm").toString());
        assertEquals("1:00pm", Temporal.parse("1:00PM").toString());
        assertEquals("1:00pm", Temporal.parse("1pm").toString());
        assertEquals("1:00pm", Temporal.parse("1PM").toString());
        assertEquals("1:00pm", Temporal.parse("13:00").toString());
        assertEquals("1:15pm", Temporal.parse("1:15pm").toString());
        assertEquals("1:15pm", Temporal.parse("1:15PM").toString());
        assertEquals("1:15pm", Temporal.parse("13:15").toString());
    }

    @Test
    public void parse_validDateTime_success() {
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("1 Jul 2024 1:00pm").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("01-07-2024 1:00pm").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("1-7-2024 1:00pm").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("2024-07-01 1:00pm").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("2024-7-1 1:00pm").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("1 Jul 2024 1pm").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("01-07-2024 1pm").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("1-7-2024 1pm").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("2024-07-01 1pm").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("2024-7-1 1pm").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("1 Jul 2024 13:00").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("01-07-2024 13:00").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("1-7-2024 13:00").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("2024-07-01 13:00").toString());
        assertEquals("1 Jul 2024 1:00pm", Temporal.parse("2024-7-1 13:00").toString());
    }

    @Test
    public void parse_invalid_textReturned() {
        String[] invalidTexts = {
            "",
            "hello",
            "32 Jul 2024",
            "13:00pm",
            "25:00",
            "1:60pm",
            "13:60",
            "1pm 1 Jul 2024"
        };
        for (String invalidText : invalidTexts) {
            assertEquals(invalidText, Temporal.parse(invalidText).toString());
        }
    }
}
