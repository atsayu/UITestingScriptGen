from http.server import HTTPServer, SimpleHTTPRequestHandler
from urllib.parse import urlparse, parse_qs
from pyeda.inter import *

class CustomHandler(SimpleHTTPRequestHandler):
    def do_GET(self):
        # Parse the URL and query parameters
        parsed_url = urlparse(self.path)
        query_params = parse_qs(parsed_url.query)

        # Check the path to determine the route
        if parsed_url.path == '/tb':
            # Handle the "/tb" route
            self.handle_truth_table(query_params)
        elif parsed_url.path == '/simplify':
            # Handle the "/simplify" route
            self.handle_simplify(query_params)
        elif parsed_url.path == '/dnf':
            # Handle the "/dnf" route
            self.handle_dnf(query_params)
        else:
            # Default response for unknown routes
            self.send_response(404)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write("Not Found".encode('utf-8'))

    def handle_truth_table(self, query_params):
        # Get the value of a specific parameter (e.g., 'param')
        param_value = query_params.get('param', [''])[0]

        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()

        # Send a response with the truth table
        response = f"{expr2truthtable(expr(param_value))}"
        self.wfile.write(response.encode('utf-8'))

    def handle_simplify(self, query_params):
        # Get the value of a specific parameter (e.g., 'param')
        param_value = query_params.get('param', [''])[0]

        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()

        # Send a response with the simplified expression
        response = f"200"
        self.wfile.write(response.encode('utf-8'))
        
    def handle_dnf(self, query_params):
        # Get the value of a specific parameter (e.g., 'param')
        param_value = query_params.get('param', [''])[0]

        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()

        # Send a response with the simplified expression
        response = f"{(expr(param_value).to_dnf())}"
        self.wfile.write(response.encode('utf-8'))

if __name__ == '__main__':
    server_address = ('', 8000)  # Listen on port 8000, change as needed
    httpd = HTTPServer(server_address, CustomHandler)
    print("Server started on port 8000")
    httpd.serve_forever()
