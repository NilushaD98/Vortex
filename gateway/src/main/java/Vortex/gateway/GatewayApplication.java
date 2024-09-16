package Vortex.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder){

		return routeLocatorBuilder
				.routes()
				.route(predicateSpec -> predicateSpec
						.path("/userservice/**")
						.uri("http://localhost:8200"))
				.route(predicateSpec -> predicateSpec
							.path("/vortexcoreservice/**")
							.uri("http://localhost:8100"))
				.route(predicateSpec -> predicateSpec
						.path("/vortexpostservice/**")
						.uri("http://localhost:8300"))
				.route(predicateSpec -> predicateSpec
						.path("/vortexnotificationservice/**")
						.uri("http://localhost:8400"))
				.route(predicateSpec -> predicateSpec
						.path("/marketplaceservice/**")
						.uri("http://localhost:8500"))
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
