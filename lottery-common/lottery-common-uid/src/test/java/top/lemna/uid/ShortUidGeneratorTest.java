package top.lemna.uid;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShortUidGeneratorTest {

  private ShortUidGenerator shortUidGenerator;

  @Mock
  private ShortUidAssigner shortUidAssigner;

  @BeforeEach
  void setUp() throws Exception {

    when(shortUidAssigner.assignWorkerId(any())).thenReturn(1L);
  }

  @Test
  void testGetUID() {
    shortUidGenerator = new ShortUidGenerator("TestShortID", 8, shortUidAssigner);
    long uid = shortUidGenerator.getUID();

    assertThat(uid, greaterThan(10000000l));
    assertThat(uid, lessThan(10000002l));
  }

}
