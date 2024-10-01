package com.example.bookstoreapi.controller;

import com.example.bookstoreapi.model.Book;
import com.example.bookstoreapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenCreateBook_thenStatus201() throws Exception {
        Book book = new Book();
        
        book.setTitle("Test Driven Development");
        book.setAuthor("Kent Beck");
        book.setIsbn("9780321146533");
        book.setGenre("Programming");
        book.setPublicationYear(Year.of(2002));

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Driven Development"))
                .andExpect(jsonPath("$.author").value("Kent Beck"));
    }

    @Test
    void whenGetAllBooks_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void whenGetBookById_thenStatus200() throws Exception {
        // First, create a book
        Book book = new Book();
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setIsbn("9780132350884");
        book.setGenre("Programming");
        book.setPublicationYear(Year.of(2008));

        String createResult = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andReturn().getResponse().getContentAsString();
        
        Book createdBook = objectMapper.readValue(createResult, Book.class);

        // Then, retrieve it by ID
        mockMvc.perform(get("/api/books/{id}", createdBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }
}