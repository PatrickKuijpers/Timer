import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import nl.tcilegnar.timer.BuildConfig;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
#parse("File Header.java")
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ${NAME} {
  ${BODY}
}