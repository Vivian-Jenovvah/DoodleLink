package com.collaborativecanvas.shared;

import com.collaborativecanvas.shared.dto.*;
import com.collaborativecanvas.shared.enums.DrawingOperationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ContractSerializationTest {
  @Test void operationRoundTrips() throws Exception {
    var mapper = new ObjectMapper().findAndRegisterModules();
    var input = new DrawingOperationMessage(UUID.randomUUID(), "ABC123", null, DrawingOperationType.DRAW_START,
      UUID.randomUUID().toString(), .2, .3, "#1E88E5", 6, false, null, null, null);
    assertEquals(input, mapper.readValue(mapper.writeValueAsString(input), DrawingOperationMessage.class));
  }
  @Test void emptyDocumentIsVersioned() { assertEquals(1, CanvasDocument.empty().schemaVersion()); }
}
