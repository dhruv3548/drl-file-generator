package drlfilegenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.goomo.drlfilegenerator.domain.Hotel;
import com.goomo.drlfilegenerator.domain.HotelBooking;

public class TestHotelBooking {
	public static void main(String[] args) {
		// load up the knowledge base
		KnowledgeBase kbase;
		try {
			String drlFilePath = "C:\\Learning\\" + "hotelsDiscount.drl";
			File file = new File(drlFilePath);
			String path = file.getAbsolutePath();
			kbase = readKnowledgeBase(path);
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
			// go !
			HotelBooking hb = new HotelBooking();
			List<Hotel> hotels = new ArrayList<Hotel>();
			for (int i = 1; i < 10; i++) {
				Hotel h = new Hotel();
				h.setHotelId(String.valueOf(i));
				h.setPrice(i * 1000);
				hotels.add(h);
			}
			hb.setListOfHotels(hotels);
			ksession.insert(hb);
			ksession.fireAllRules();
			logger.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static KnowledgeBase readKnowledgeBase(String drlFilePath) throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newFileResource(drlFilePath), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}
}
