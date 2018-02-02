package org.k9.modulename

class One implements Serializable {
  def config
  def script

  One(script,config) {
    this.config = config
    this.script = script
  }

  
  void wish()
  {
	  this.script.stage('wish'){
		this.script.sh('echo Hello ' + this.config.name)
	}	
  }

}
