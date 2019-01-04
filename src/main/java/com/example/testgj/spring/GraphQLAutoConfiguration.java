package com.example.testgj.spring;

import com.example.testgj.servlet.GraphQLPlaygroundServlet;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.servlet.GraphQLConfiguration;
import graphql.servlet.GraphQLHttpServlet;
import graphql.servlet.GraphQLInvocationInputFactory;
import graphql.servlet.GraphQLObjectMapper;
import graphql.servlet.GraphQLQueryInvoker;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.servlet.http.HttpServlet;
import java.io.IOException;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

@Configuration
public class GraphQLAutoConfiguration {


  @Bean
  public GraphQLSchema graphQLSchema() {
    String schema =  "type Query{hello: String}";
    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

    RuntimeWiring runtimeWiring = newRuntimeWiring()
        .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
        .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
  }

  @Bean
  public GraphQLQueryInvoker graphQLQueryInvoker() {
    return GraphQLQueryInvoker.newBuilder().build();
  }

  @Bean
  public GraphQLInvocationInputFactory graphQLInvocationInputFactory(
      GraphQLSchema graphQLSchema
      //GraphQLContextBuilder graphQLContextBuilder
  ) {
    return GraphQLInvocationInputFactory.newBuilder(graphQLSchema).build();
  }

  @Bean
  public GraphQLObjectMapper graphQLObjectMapper() {
    return GraphQLObjectMapper.newBuilder().build();
  }

  @Bean
  public GraphQLConfiguration graphQLConfiguration(
      GraphQLInvocationInputFactory graphQLInvocationInputFactory,
      GraphQLQueryInvoker graphQLQueryInvoker,
      GraphQLObjectMapper graphQLObjectMapper
  ) {
    return GraphQLConfiguration.with(graphQLInvocationInputFactory)
        .with(graphQLQueryInvoker)
        .with(graphQLObjectMapper).build();
  }

  @Bean
  public ServletRegistrationBean graphQLServletRegistration(GraphQLHttpServlet graphQLServlet) {
    return new ServletRegistrationBean(graphQLServlet, "/graphql");
  }

  @Bean
  public ServletRegistrationBean playgroundServletRegistration() throws IOException {
    return new ServletRegistrationBean(
      new GraphQLPlaygroundServlet(),
        "/playground");
  }


  @Bean
  public GraphQLHttpServlet graphQLHttpServlet(GraphQLConfiguration graphQLConfiguration) {
    return GraphQLHttpServlet.with(graphQLConfiguration);
  }
}
