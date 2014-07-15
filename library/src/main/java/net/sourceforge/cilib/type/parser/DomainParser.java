/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.parser;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;

import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

/**
 * The domain parser converts a provided domain string representation into
 * a {@code StructuredType}. If the domain string defines a simple {@code Vector}
 * based representation of {@code Numeric} types, then a {@code Vector} is returned.
 */
public final class DomainParser {

    private final static DomainParserGrammar.ExpandingParser EXPANDING_PARSER = Parboiled.createParser(DomainParserGrammar.ExpandingParser.class);
    private final static DomainParserGrammar.DomainGrammar DOMAIN_PARSER = Parboiled.createParser(DomainParserGrammar.DomainGrammar.class);

    private DomainParser() {
    }

    /**
     * Parse the provided domain string and return the constructed representation.
     * @param <E> The structured type.
     * @param domain The string to parse.
     * @return A {@code TypeList} is returned by default, but if the type is defined
     *         to consist of {@code Numeric} types, a {@code Vector} instance is returned.
     */
    public synchronized static <E extends StructuredType<? extends Type>> E parse(String domain) {
    	
        final ReportingParseRunner<String> expander = new ReportingParseRunner<String>(EXPANDING_PARSER.Expansion());
        final ParsingResult<String> d = expander.run(domain.replaceAll(" ", ""));
        
        if (d.hasErrors()) {
            StringBuilder strBuilder = new StringBuilder();
            for (ParseError e : d.parseErrors) {
                strBuilder.append(e.getInputBuffer().extract(e.getStartIndex(), e.getEndIndex()));
            }
            throw new RuntimeException("Error in expanding domain: " + domain + " near: " + strBuilder.toString());
        }
        
//        System.out.println(d.valueStack.size());
//        System.out.println("Creating vector");
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < d.valueStack.size(); i++) {
			builder.add(Real.valueOf(0));
		}
        Vector myVector = builder.build();
//        System.out.println("Created vector of size: " + myVector.size());
        
        return (E) myVector;
//        .add(Real.valueOf(1.0)).add(Real.valueOf(2.0)).add(Real.valueOf(3.0));
//        tmpVector = builder.build();
        
//        System.out.println(d.valueStack.peek());
//        final String expanded = Joiner.on(",").join(d.valueStack);
//        final ReportingParseRunner<?> runner = new ReportingParseRunner(DOMAIN_PARSER.Domain());
//        
//        System.out.println("Before Fail");
//        System.out.println(expanded);
//        final ParsingResult<Type> result = (ParsingResult<Type>) runner.run(expanded);
//        System.out.println("in parser");
//        if (result.hasErrors()) {
//            StringBuilder strBuilder = new StringBuilder();
//            for (ParseError e : result.parseErrors) {
//                strBuilder.append(e.getInputBuffer().extract(e.getStartIndex(), e.getEndIndex()));
//            }
//            throw new RuntimeException("Error in parsing domain: " + expanded +
//                    ". Ensure that the domain is a valid domain string and contains no whitespace.\nError occurred near: " + strBuilder.toString());
//        }
//        System.out.println(result.valueStack.size());
//        System.out.println(result.valueStack.peek());
//        System.out.println(result.valueStack.peek(1));
//        System.out.println("in parser");
//        System.out.println(result.valueStack);
//        List<Type> l = Lists.newArrayList(result.valueStack);
//        System.out.println("in parser");
//        if (isVector(l)) {
//        	System.out.println("is vector");
//            @SuppressWarnings("unchecked")
//            E vector = (E) toVector(l);
//            return vector;
//        }
//
//        return (E) toTypeList(l);
    }

    private static TypeList toTypeList(List<Type> l) {
        TypeList list = new TypeList();
        for (Type t : l) {
            list.add(t);
        }
        return list;
    }

    /**
     * Convert the {@code TypeList} into a {@code Vector} if all elements are
     * {@code Numeric} types.
     *
     *
     * @param representation The current data structure of the representation.
     * @return {@code true} if the structure should really be a {@code Vector},
     *         {@code false} otherwise.
     */
    private static boolean isVector(List<Type> representation) {
        for (Type type : representation) {
            if (!(type instanceof Numeric)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Convert the provided {@code representation} into a {@code Vector}.
     *
     * @param representation The {@code StructuredType} to convert.
     * @return The converted vector object.
     */
    private static Vector toVector(List<Type> representation) {
        Vector.Builder vector = Vector.newBuilder();

        for (Type type : representation) {
//        	Numeric n = new ;
//        	System.out.println(n);
        	
//        	vector.add(Numeric);
            vector.add((Numeric) type);
        }

        return vector.build();
    }
}
