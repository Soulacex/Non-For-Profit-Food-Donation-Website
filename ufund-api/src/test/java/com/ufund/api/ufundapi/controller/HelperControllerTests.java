// package com.ufund.api.ufundapi.controller;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.doThrow;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.when;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import java.io.IOException;
// import java.util.ArrayList;

// import com.ufund.api.ufundapi.model.Needs;
// import com.ufund.api.ufundapi.persistence.HelperDAO;

// public class HelperControllerTests {

//     private HelperController helperController;
//     private HelperDAO mockHelperDAO;
//     /**
//      * Before each test, generate a new ManagerController object and inject a mock DAO
//      */
//     @BeforeEach
//     public void setUp() {
//         mockHelperDAO = mock(HelperDAO.class);
//         helperController = new HelperController(mockHelperDAO);
//     }

//     @Test
//     void getAllNeeds() throws IOException{
//         // Setup
//         Needs[] needs= new Needs[3];
//         needs[0] = new Needs("Fo", 10, "Canned", 90);
//         needs[1] = new Needs("Fool", 8, "Canned", 31);
//         needs[2] = new Needs("Food", 1, "Canned", 0);
//         when(mockHelperDAO.getAllNeeds()).thenReturn(needs);

//         // Invoke
//         ResponseEntity<Needs[]> responseEntity = helperController.getAllNeeds();

//         // Analyze
//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//         assertEquals(needs,responseEntity.getBody());
//      }

//     @Test
//     public void getAllNeedsIOException() throws IOException {
//         // Setup
//         Needs[] needs= new Needs[3];
//         needs[0] = new Needs("Fo", 10, "Canned", 90);
//         needs[1] = new Needs("Fool", 8, "Canned", 31);
//         needs[2] = new Needs("Food", 1, "Canned", 0);
//         doThrow(new IOException()).when(mockHelperDAO).getAllNeeds();

//         // Invoke
//         ResponseEntity<Needs[]> responseEntity = helperController.getAllNeeds();

//         // Analyze
//         assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
//     }

//     @Test
//     public void addSupportedNeed() throws IOException{
//         // Setup
//         Needs need = new Needs("Food", 2, "Canned", 4);
//         when(mockHelperDAO.addSupportedNeed(need.getName(), need.getQuantity())).thenReturn(need);

//         // Invoke
//         ResponseEntity<Needs> responseEntity = helperController.addSupportedNeed(need.getName(), need.getQuantity());

//         // Analyze
//         assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//         assertEquals(need,responseEntity.getBody());
//     }

//     @Test
//     public void addSupportedNeedFailure() throws IOException{
//         // Setup
//         Needs need = new Needs("Foo", 2, "Canned", -5);
//         when(mockHelperDAO.addSupportedNeed(need.getName(), need.getQuantity())).thenReturn(null);

//         // Invoke
//         ResponseEntity<Needs> responseEntity = helperController.addSupportedNeed(need.getName(), need.getQuantity());

//         // Analyze
//         assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
//     }

//     @Test
//     public void addFoodNeedIOException() throws IOException {
//         // Setup
//         Needs need = new Needs("Fo", 10, "Canned", 90);
//         doThrow(new IOException()).when(mockHelperDAO).addSupportedNeed(need.getName(),need.getQuantity());

//         // Invoke
//         ResponseEntity<Needs> responseEntity = helperController.addSupportedNeed(need.getName(), need.getQuantity());

//         // Analyze
//         assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
//     }

//     @Test
//     //Delete needs does not return correctly
//     public void removeSupportedNeed() throws IOException{
//         // Setup
//         Needs need = new Needs("Food", 2, "Canned", 4);
//         when(mockHelperDAO.removeSupportedNeed(need.getName())).thenReturn(need);
        
//         // Invoke
//         ResponseEntity<Needs> responseEntity = helperController.removeSupportedNeed(need.getName());

//         // Analyze
//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//     }
    
//     @Test
//     public void removeSupportNeedFailure() throws IOException{
//         // Setup
//         Needs need = new Needs("Food", 2, "Canned", 4);
//         when(mockHelperDAO.removeSupportedNeed(need.getName())).thenReturn(need);
        
//         // Invoke
//         ResponseEntity<Needs> responseEntity = helperController.removeSupportedNeed("bap");

//         // Analyze
//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//     }

//     @Test
//     public void deleteNeedIOException() throws IOException {
//         // Setup
//         String needName = "Food";
//         doThrow(new IOException()).when(mockHelperDAO).removeSupportedNeed(needName);

//         // Invoke
//         ResponseEntity<Needs> responseEntity = helperController.removeSupportedNeed(needName);

//         // Analyze
//         assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
//     }

//     @Test
//     void searchPartialName() throws IOException{
//         // Setup
//         String testSearchString = "oo";
//         ArrayList<Needs> needs= new ArrayList<>();
//         needs.add(0, new Needs("Fo", 10, "Canned", 90));
//         needs.add(1, new Needs("Fool", 8, "Canned", 31));
//         needs.add(2, new Needs("Food", 1, "Canned", 0));
//         when(mockHelperDAO.browseNeeds(testSearchString)).thenReturn(needs);

//         // Invoke
//         ResponseEntity<ArrayList<Needs>> responseEntity = helperController.searchPartialName(testSearchString);

//         // Analyze
//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//         assertEquals(needs,responseEntity.getBody());
//     }

//     @Test
//     public void searchPartialNameIOException() throws IOException {
//         // Setup
//         String testSearchString = "oo";
//         ArrayList<Needs> needs= new ArrayList<>();
//         needs.add(0, new Needs("Fo", 10, "Canned", 90));
//         needs.add(1, new Needs("Fool", 8, "Canned", 31));
//         needs.add(2, new Needs("Food", 1, "Canned", 0));
//         doThrow(new IOException()).when(mockHelperDAO).browseNeeds(testSearchString);

//         // Invoke
//         ResponseEntity<ArrayList<Needs>> responseEntity = helperController.searchPartialName(testSearchString);

//         // Analyze
//         assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
//     }

//     @Test
//     public void checkOut() throws IOException{
//         // Setup
//         when(mockHelperDAO.checkOut()).thenReturn(true);

//         // Invoke
//         ResponseEntity<Needs> responseEntity = helperController.checkOut();

//         // Analyze
//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//     }

//     @Test
//     public void checkOutFailure() throws IOException{
//         // Setup
//         when(mockHelperDAO.checkOut()).thenReturn(false);

//         // Invoke
//         ResponseEntity<Needs> responseEntity = helperController.checkOut();

//         // Analyze
//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//     }

//     @Test
//     public void checkOutIOException() throws IOException {
//         // Setup
//         doThrow(new IOException()).when(mockHelperDAO).checkOut();

//         // Invoke
//         ResponseEntity<Needs> responseEntity = helperController.checkOut();

//         // Analyze
//         assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
//     }
// }

