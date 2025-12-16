# AI Collaboration Challenge - Bulk Operations Feature

## Feature Request
Add a product update feature that allows users to perform basic update actions (price updates, category changes, deletion) with appropriate confirmation responses and error handling.

## 1. AI Tool Selection

**Which AI tool would you choose and why?**

[
    I would choose ChatGPT (GPT-4 or later) as the AI collaboration tool.
    Reasoning
    -It has strong context retention, which is essential for understanding layered architectures like Controller → Service Interface → Service Implementation → Repository.
    -It supports iterative refinement, allowing me to progressively enhance solutions.
    -It excels at producing production-ready code while adhering to constraints such as HTTP semantics, interface-based design, and standardized error responses.
    -And it is faster in terms of generation response
]

## 2. Comprehensive Prompt

**Write your complete prompt including context about the codebase architecture and any constraints:**

[
    I have a product table and I am sharing the entity against my product table below
    @Entity
    public class Product {
        @Id  
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String name;
        @Column(nullable = false)
        private double price;
        @Column(nullable = false)
        private String category;
        @Column(nullable = false)
        private boolean available;
    }
    now I need to implement a feature where users can perform basic update actions like price updates, category changes & deletion of a product. 
    I need three api. One for updating product's price, one for updating product's category and lastly to delete a product. 
    All the three api will use product's id in the path variable. I want an interface-based OOP implementation. 
    There will be an interface called "ProductService" where all method signatures will be defined, and a "ProductServiceImpl" class 
    that implements the "ProductService" interface.
    In the controller I will just use the interface to call the service layer methods. 
    Use MapStruct for DTO to entity conversion and vice versa.
    I also need two custom exceptions:
    One for NoDataFound when the given product ID does not exist in the database, which should return HTTP status 404
    Another for the case where the price is negative, which should return HTTP status 400
    For exceptions, the response format should be:
    {
    "error": "VALIDATION_ERROR",
    "message": "appropriate message",
    "field": "price"
    }
    For a successful product update, return a confirmation response with HTTP status 200:
    {
    "message": "product updated successfully"
    }
    For deletion, return HTTP status 204 (No Content) with no response body.
    Please make sure the entire solution is implemented using Java and Spring Boot.
]

## 3. Collaboration Approach

**How would you iterate and collaborate with the AI tool to implement this feature?**

[
I would use the AI tool in an iterative and controlled manner, treating it as a development assistant rather than a single-shot solution.
    
 My approach would be:

 -Start with a clear architectural prompt:
    --Provide context about the existing Spring Boot architecture (controller-service-repository pattern, MapStruct usage, exception handling strategy) and clearly define constraints.
    
    -Implement incrementally:
    --First, ask the AI to generate service interfaces and method contracts.
    --Next, implement service implementations with business validation
    --Then add custom exceptions and global exception handling.
    --Finally, implement the controller layer and validate HTTP status codes.
    
 -Validate against requirements:
    After each iteration, I would:
    --Review code for REST correctness (HTTP 200 vs 204 vs 400 vs 404).
    --Ensure separation of concerns (no business logic in controllers).
    --Confirm consistency in error and success response formats.
 
 -Refine and harden the solution:
    Use follow-up prompts to:
    --Improve naming conventions
    --Reduce duplication
    --Align with Spring Boot best practices
    --Ensure maintainability and extensibility

 -Final human review:
    Before considering the solution complete, I would manually review and test the code, ensuring it aligns with real-world production standards.
]
