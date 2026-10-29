package com.collaborativecanvas.client.drawing;
import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
class CoordinateMapperTest {@Test void normalizesWithinLetterbox(){var b=CoordinateMapper.drawableBounds(1000,1000,1200,800);var p=CoordinateMapper.normalized(500,500,b);assertEquals(.5,p.normalizedX(),.001);assertEquals(.5,p.normalizedY(),.001);assertTrue(CoordinateMapper.contains(500,500,b));}}
