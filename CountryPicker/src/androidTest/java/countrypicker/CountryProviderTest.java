package countrypicker;

import android.test.InstrumentationTestCase;

import com.countrypicker.Country;
import com.countrypicker.CountryProvider;

import org.json.JSONException;

import java.io.IOException;

/**
 * @author Michael Ruf
 * @since 2015-09-04
 */
public class CountryProviderTest extends InstrumentationTestCase {

    public void testDataLoading() throws IOException, JSONException {
        CountryProvider cp = new CountryProvider(getInstrumentation().getTargetContext());
        assertTrue(cp.getCountries().size() > 0);
    }

    public void testGetCountryByCode() throws IOException, JSONException {
        CountryProvider cp = new CountryProvider(getInstrumentation().getTargetContext());

        Country de = cp.getCountryByCode("DE");
        assertNotNull(de);
        assertEquals("DE", de.getCode());
        assertEquals("Germany", de.getName());

        Country nl = cp.getCountryByCode("nl");
        assertNotNull(nl);
        assertEquals("NL", nl.getCode());
        assertEquals("Netherlands", nl.getName());
    }
}
