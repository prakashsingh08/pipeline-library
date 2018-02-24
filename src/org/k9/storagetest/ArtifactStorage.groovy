package org.k9.storagetest

@Grapes([
 @Grab (group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.5.0'),
 @Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7'),
 @Grab('org.apache.httpcomponents:httpmime:4.5.1')
])

import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.JSON
import java.io.File 
import groovyx.net.http.HTTPBuilder

//by prakash
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.content.InputStreamBody
import org.apache.http.entity.mime.content.StringBody
import groovyx.net.http.*
	

class ArtifactStorage implements Serializable {
  def config
  def script

  ArtifactStorage(script,config) {
    this.config = config
    this.script = script
  }

  boolean bucketValidation(){
	def bucketValidation=false 
	def url = "https://www.googleapis.com"
	def client = new RESTClient(url)
	def response = client.get(path: "/storage/v1/b",
		query: [project:this.config.project],
		headers: [Authorization: 'Bearer '+ this.config.accesstoken]
		)
 	this.script.echo "1.1"
        def slurper = new JsonSlurper().parseText(response.data.toString())
	List bucketList = slurper.items
	bucketList.each{
    	    if(bucketValidation==false)
	    {
		this.script.echo it.id
                if(it.id == this.config.bucket)
                {
                   bucketValidation=true
		  this.script.echo "1.2"
                }
            }
        }
	this.script.echo "1.3"
	return bucketValidation
  }

  @NonCPS 
  def upload() {
    this.script.stage('Upload Artifcat') {
	if(this.config.storage=='Bucket')
	{
		this.script.echo "1"
		if(true){
			//curl -X POST --data-binary @image.png     -H "Authorization: Bearer ya29.GltoBUSTPauiAI2vzTAB5c2KaWKSKX460xES6y0FDStC8ETAEDYdb15l3Um3laRY1WqPrbWVD1nV-ZbEh9-hAOMhybcEIX-R0LRXI0w_kL7A72DWk2EmlQlGP55a"     -H "Content-Type: image/png"     "https://www.googleapis.com/upload/storage/v1/b/jas-1893/o?uploadType=media&name=output.png"

//def str=this.script.sh(script: 'curl -X POST --data-binary @/tmp/workspace/new/target/spring-boot-rest-example-0.5.0.war     -H "Authorization: Bearer ya29.GlxqBYhJRtEjqRjq8KRfHm5uKFSWB6YfDfeTjB4xDYk6s6BPgjiXlH76B4A2gYTgv16VSUQlOpFHNaIHMpRCbTcftHuvzoAGRYHtKQvu-ELY6QTVQ907iwJzoZxZnA"     -H "Content-Type: application/java-archive"     "https://www.googleapis.com/upload/storage/v1/b/jas-1893/o?uploadType=media&name=a.war"' , returnStdout: true)

//			this.script.echo "2"
//			def url = "https://www.googleapis.com"
//			def client = new RESTClient(url)
//			def client = new HTTPBuilder(url)
	//		this.script.echo "3"
	//		this.script.echo "${this.script.env.WORKSPACE}"
			//def file1 = new File("/tmp/workspace/new/target/spring-boot-rest-example-0.5.0.war")
        //		def response = client.post(path: "/upload/storage/v1/b/"+this.config.bucket+"/o",
   	//	     		query: [uploadType: 'media', name: 'output-222.png'],
	//			body: [file: new File("/home/psingh_singh361/test/spring-boot-web-jsp-1.0.war")],
	//                        body: [file: new File("/home/psingh_singh361/test/output.png")],
       	//			headers: [Authorization: 'Bearer '+ this.config.accesstoken, "Content-Type" : "image/png"]
        //			)
			
			
			
			
			
			
			// by prakash
			 
 def http = new HTTPBuilder("https://www.googleapis.com")
 def file = new File("/home/psingh_singh361/test/output.png")
 
 http.request(Method.POST) { req ->
 uri.path = "/upload/storage/v1/b/"+this.config.bucket+"/o"
//query.uploadType = 'media' 
//query.name = 'output111.png'

uri.query = [uploadType = 'media', name = 'output111.png']
uri.headers = [Authorization: 'Bearer '+ this.config.accesstoken, "Content-Type" : "image/png"]

 //headers.'Authorization' = 'Bearer '+this.config.accesstoken
 //headers.'Content-Type' = 'image/png'
 
 requestContentType: "multipart/form-data"
 
 MultipartEntity multiPartContent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
 
 // Adding Multi-part file parameter "imageFile"
 
 multiPartContent.addPart("png", new FileBody(( File ) file, 'image/jpeg' ))
 

 
 req.setEntity(multiPartContent)
 
 response.success = { resp ->
 
        if (resp.statusLine.statusCode == 200) {
 
                  // response handling
 
                   }
            }
      }
			
			
			
			
			
			
			
			this.script.echo "4"
			this.script.echo response.status 
			return reponse.data
			//return str
		}	
	}
	else{
		this.script.sh("curl -v -u ${this.config.nexus_user}:${this.config.nexus_pass} --upload-file ${this.script.env.WORKSPACE}/target/*.${this.config.artifacts_type} ${this.config.artifacts_url}/repository/${this.config.repository}/${artifacts_file}")
     }
   }
	
  }
  void download() {
    this.script.stage('Download Artifcat') {
        def artifacts_file = "${this.script.env.BUILD_NUMBER}_${this.config.stage}_${this.config.namespace}.${this.config.artifacts_type}"
        this.script.sh("curl -o ${artifacts_file} -u ${this.config.nexus_user}:${this.config.nexus_pass} ${this.config.artifacts_url}/repository/${this.config.repository}/${artifacts_file}")
    }
  }
}
