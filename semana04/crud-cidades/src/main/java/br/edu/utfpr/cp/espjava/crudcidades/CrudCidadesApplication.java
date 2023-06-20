package br.edu.utfpr.cp.espjava.crudcidades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.Validator;

/* Semana 04 - Separando a mensagem do código
 * As mensagens estão no arquivo messages.properties. 
 * O arquivo tem um formato par de valores. Do lado esquerdo é definida uma chave e do lado direito seu valor. A chave será usada lá nas anotações de validação 
 * da classe Cidade para associar a mensagem que deve ser usada em cada caso.
 * 
 */
@SpringBootApplication
public class CrudCidadesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudCidadesApplication.class, args);
	}

	/* Esse método define o nome do arquivo (classpath:messages) e o tipo de codificação que queremos usar no arquivo onde estarão armazenadas as mensagens (UTF-8) 
	 * A anotation @Bean sinaliza que o Spring Boot deve gerenciar esse método.
	*/
	@Bean
	public MessageSource messageSource(){
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	/* Esse método aqui registra o arquivo de mensagens para que ele seja usado juntamente com as validações */
	@Bean
	public Validator getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}

}
