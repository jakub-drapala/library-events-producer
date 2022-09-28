package com.example.libraryeventsproducer;

import com.example.libraryeventsproducer.controller.LibraryEventsController;
import com.example.libraryeventsproducer.domain.Book;
import com.example.libraryeventsproducer.domain.LibraryEvent;
import com.example.libraryeventsproducer.producer.LibraryEventProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LibraryEventProducer libraryEventProducer;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void postLibraryEvent() throws Exception {
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor("Drapala")
                .bookName("Kafka book")
                .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);
        doNothing().when(libraryEventProducer).sendLibraryEvent_Approach2(isA(LibraryEvent.class));

        mockMvc.perform(post("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void postLibraryEvent_4xx() throws Exception {
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor(null)
                .bookName("Kafka book")
                .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);
        doNothing().when(libraryEventProducer).sendLibraryEvent_Approach2(isA(LibraryEvent.class));

        mockMvc.perform(post("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void putLibraryEvent() throws Exception {
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor("Drapala")
                .bookName("Kafka book")
                .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(1)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);
        when(libraryEventProducer.sendLibraryEvent_Approach2(isA(LibraryEvent.class)))
                .thenReturn(null);

        mockMvc.perform(put("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void putLibraryEvent_4xx() throws Exception {
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor("Drapala")
                .bookName("Kafka book")
                .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        mockMvc.perform(put("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());


    }
}
